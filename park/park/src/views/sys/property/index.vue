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
        <el-input v-model="listQuery.propertyName" placeholder="输入公司名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="initData">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="topictable" v-loading="loading" :height="tableHeight" :row-style="rowStyle" size="mini" border :data="tableData" @selection-change="selectionChangeHandle">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="propertyId" label="ID" width="80" sortable align="center" />
      <el-table-column prop="propertyCode" label="公司编号" width="200" show-overflow-tooltip align="center" />
      <el-table-column prop="propertyName" label="公司名称" width="200" show-overflow-tooltip align="center" />
      <el-table-column prop="contact" label="联系人" width="180" show-overflow-tooltip align="center" />
      <el-table-column prop="phone" label="联系电话" width="180" show-overflow-tooltip align="center" />
      <el-table-column prop="status" label="状态" width="130" show-overflow-tooltip align="center" />
      <el-table-column prop="createTime" label="注册时间" width="210" align="center" />
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="initData" />
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="initData" />
    <ImportExcel v-if="importVisible" ref="importExcel" @refreshDataList="initData" />
  </div>
</template>

<script>
import { getList, del } from '@/api/sys/property'
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
        propertyName: ''
      },
      total: 0,
      loading: true,
      downloadLoading: false,
      ids: [],
      tableData: [],
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
        this.tableData = res.data.list
        this.total = res.data.totalCount
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
          this.$refs.addOrUpdate.init(this.ids[0].propertyId)
        })
      } else {
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init()
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
        return item.propertyId
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
