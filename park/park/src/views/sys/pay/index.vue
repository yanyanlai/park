<template>
  <div class="app-container">
    <el-form :inline="true" :model="listQuery" @keyup.enter.native="initData()">
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-download" @click="handleExcel">导出</el-button>-->
<!--      </el-form-item>-->
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
        <el-select v-model="listQuery.parkId" filterable placeholder="请选择" style="width:150px" clearable>
          <el-option
            v-for="park in parkList"
            :key="park.parkId"
            :label="park.parkName"
            :value="park.parkId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="车牌号">
        <el-input v-model="listQuery.number" placeholder="输入车牌号" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="initData">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="topictable" v-loading="loading" :height="tableHeight" :row-style="rowStyle" size="mini" border :data="tableData" @selection-change="selectionChangeHandle">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="payRecordId" label="ID" width="100" sortable align="center" />
      <el-table-column prop="propertyName" label="公司名称" width="180" show-overflow-tooltip align="center" />
      <el-table-column prop="parkName" label="停车场名称" width="180" show-overflow-tooltip align="center" />
      <el-table-column prop="number" label="车牌号" width="180" show-overflow-tooltip align="center" />
      <el-table-column prop="payType" label="车辆类型" width="180" show-overflow-tooltip align="center" />
      <el-table-column prop="amount" label="支付金额" width="180" show-overflow-tooltip align="center" />
      <el-table-column prop="createTime" label="支付时间" width="180" show-overflow-tooltip align="center" />
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="initData" />
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="initData" />
    <ImportExcel v-if="importVisible" ref="importExcel" @refreshDataList="initData" />
  </div>
</template>

<script>
import { exportExcel, getList, del } from '@/api/sys/pay'
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
        parkId: '',
        number: ''
      },
      total: 0,
      loading: true,
      downloadLoading: false,
      ids: [],
      tableData: [],
      propertyList: [],
      parkList: [],
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
        this.parkList = res.data.parkList
        this.loading = false
      })
    },
    // 导出
    handleExcel() {
      this.$confirm('是否确认导出所有数据项?').then(res => {
        this.downloadLoading = true
        exportExcel(this.listQuery).then(res => {
          location.href = process.env.VUE_APP_BASE_API + '/park/upload/excel/' + res.data
        })
      }).catch(() => {})
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
          this.$refs.addOrUpdate.init(this.ids[0].carId)
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
        return item.carId
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
