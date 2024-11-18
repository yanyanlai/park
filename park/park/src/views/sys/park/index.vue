<template>
  <div class="app-container">
    <el-form :inline="true" :model="listQuery" @keyup.enter.native="initData()">
      <el-form-item>
        <el-button v-has="'sys:community:add'" type="primary" icon="el-icon-plus" @click="addOrUpdateHandle(1)">添加</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-has="'sys:community:info'" type="success" icon="el-icon-edit" :disabled="ids.length <= 0" @click="addOrUpdateHandle()">编辑</el-button>
      </el-form-item>
      <el-form-item>
        <el-button v-has="'sys:community:del'" type="danger" icon="el-icon-delete" :disabled="ids.length <= 0" @click="deleteHandle()">删除</el-button>
      </el-form-item>
      <el-form-item label="公司名称">
        <el-select v-model="listQuery.propertyId" filterable placeholder="请选择" style="width:150px" clearable>
          <el-option
            v-for="park in propertyList"
            :key="park.propertyId"
            :label="park.propertyName"
            :value="park.propertyId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="停车场名称">
        <el-input v-model="listQuery.parkName" placeholder="输入停车场名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="initData">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="topictable" v-loading="loading" :height="tableHeight" :row-style="rowStyle" size="mini" border :data="tableData" @selection-change="selectionChangeHandle">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="parkId" label="ID" width="60" sortable align="center" />
      <el-table-column prop="propertyName" label="公司名称" width="150" show-overflow-tooltip align="center" />
      <el-table-column prop="parkName" label="停车场名称" width="150" show-overflow-tooltip align="center" />
      <el-table-column prop="parkCount" label="车位总数" width="100" show-overflow-tooltip align="center" />
      <el-table-column prop="monthlyPrice" label="包月费用" width="100" show-overflow-tooltip align="center" />
      <el-table-column prop="freeDuration" label="免费时长" width="100" show-overflow-tooltip align="center" />
      <el-table-column prop="chargeUnit" label="计费单位" width="100" show-overflow-tooltip align="center" />
      <el-table-column prop="chargePrice" label="计费单价" width="100" show-overflow-tooltip align="center" />
      <el-table-column prop="maxCharge" label="最大金额" width="100" show-overflow-tooltip align="center" />
      <el-table-column prop="lng" label="经度" width="110" show-overflow-tooltip align="center" />
      <el-table-column prop="lat" label="纬度" width="110" show-overflow-tooltip align="center" />
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="initData" />
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="initData" />
    <ImportExcel v-if="importVisible" ref="importExcel" @refreshDataList="initData" />
  </div>
</template>

<script>
import { getList, del } from '@/api/sys/park'
import Pagination from '@/components/Pagination'
import AddOrUpdate from './add-or-update'

export default {
  name: 'Property',
  components: {
    Pagination,
    AddOrUpdate
  },
  data() {
    return {
      listQuery: {
        page: 1,
        limit: 10,
        propertyId: '',
        parkName: ''
      },
      total: 0,
      loading: true,
      downloadLoading: false,
      ids: [],
      tableData: [],
      propertyList: [],
      addOrUpdateVisible: false,
      importVisible: false
    }
  },
  created() {
    this.initData()
  },
  mounted() {
    // console.log(window.innerHeight)
    // console.log(this.$refs.topictable.$el)
    // console.log(this.$refs.topictable.$el.offsetTop)
    this.tableHeight = window.innerHeight - this.$refs.topictable.$el.offsetTop - 80
    // console.log(this.tableHeight)
  },
  methods: {
    initData() {
      this.loading = true

      getList(this.listQuery).then(res => {
        this.tableData = res.data.pageList.list
        this.total = res.data.pageList.totalCount
        this.propertyList = res.data.propertyList
        this.loading = false
      })
    },
    // 多选
    selectionChangeHandle(val) {
      this.ids = val
    },
    // 添加和编辑
    addOrUpdateHandle(flag) {
      this.addOrUpdateVisible = true
      if (flag !== 1) {
        if (this.ids.length > 1) {
          this.$message.error('编辑操作只能选择单条数据')
          return
        }
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(this.ids[0].parkId)
        })
      } else {
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(0)
        })
      }
    },
    cameraHandle() {
      if (this.ids.length > 1) {
        this.$message.error('摄像头管理只能选择单条数据')
        return
      }
      this.$nextTick(() => {
        this.$router.push({ path: '/camera', query: { communityId: this.ids[0].communityId }})
      })
    },
    // 删除
    deleteHandle(id) {
      const userIds = id ? [id] : this.ids.map(item => {
        return item.parkId
      })
      this.$confirm('确定删除 [id='+userIds+'] 的数据?').then(res => {
        del(userIds).then(res => {
          console.log(res)
          this.loading = true
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.loading = false
            this.initData()
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {})
    },
    rowStyle({ row, rowIndex }) {
      if (this.ids.includes(row)) {
        return { 'background-color': 'rgb(185, 211, 249)' }
      }
    }
  }
}
</script>
