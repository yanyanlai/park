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
      <el-form-item>
        <el-button v-has="'sys:community:info'" type="info" icon="el-icon-postcard" :disabled="ids.length <= 0" @click="payHandle()">续费</el-button>
      </el-form-item>
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-upload2" @click="importExcelHandle">导入</el-button>-->
<!--      </el-form-item>-->
      <br/>
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
      <el-form-item label="车主姓名">
        <el-input v-model="listQuery.name" placeholder="输入车主姓名" style="width: 150px;" clearable />
      </el-form-item>
      <el-form-item label="车牌号">
        <el-input v-model="listQuery.number" placeholder="输入车牌号" style="width: 150px;" clearable />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="listQuery.phone" placeholder="输入手机号" style="width: 150px;" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="initData">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="topictable" v-loading="loading" :height="tableHeight" :row-style="rowStyle" size="mini" border :data="tableData" @selection-change="selectionChangeHandle">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="carId" label="ID" width="60" sortable align="center" />
      <el-table-column prop="propertyName" label="公司名称" width="130" show-overflow-tooltip align="center" />
      <el-table-column prop="parkName" label="停车场名称" width="130" show-overflow-tooltip align="center" />
      <el-table-column prop="number" label="车牌号" width="130" show-overflow-tooltip align="center" />
      <el-table-column prop="name" label="车主姓名" width="80" show-overflow-tooltip align="center" />
      <el-table-column prop="gender" label="性别" width="60" show-overflow-tooltip align="center" />
      <el-table-column prop="phone" label="手机号码" width="130" show-overflow-tooltip align="center" />
      <el-table-column prop="status" label="状态" width="100" show-overflow-tooltip align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status == '正常'" size="mini" type="success">正常</el-button>
          <el-button v-if="scope.row.status == '欠费'" size="mini" type="danger">欠费</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="effectTime" label="有效期" width="180" show-overflow-tooltip align="center" />
      <el-table-column prop="space" label="已购车位" width="80" show-overflow-tooltip align="center" />
      <el-table-column prop="remark" label="备注" width="100" show-overflow-tooltip align="center" />
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="initData" />
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="initData" />
    <ImportExcel v-if="importVisible" ref="importExcel" @refreshDataList="initData"/>
    <pay v-if="payVisible" ref="pay" @refreshDataList="initData" />
  </div>
</template>

<script>
import { getList, del } from '@/api/sys/car'
import Pagination from '@/components/Pagination'
import AddOrUpdate from './add-or-update'
import ImportExcel from './importExcel'
import Pay from './pay'

export default {
  name: 'Property',
  components: {
    Pagination,
    AddOrUpdate,
    ImportExcel,
    Pay
  },
  data() {
    return {
      listQuery: {
        page: 1,
        limit: 10,
        propertyId: '',
        parkId: '',
        name: '',
        number: '',
        phone: ''
      },
      total: 0,
      loading: true,
      downloadLoading: false,
      ids: [],
      tableData: [],
      propertyList: [],
      parkList: [],
      addOrUpdateVisible: false,
      importVisible: false,
      payVisible: false,
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
    // 导入
    importExcelHandle() {
      this.importVisible = true
      this.$nextTick(() => {
        this.$refs.importExcel.init()
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
          this.$refs.addOrUpdate.init(this.ids[0].carId)
        })
      } else {
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(0)
        })
      }
    },
    //续费
    payHandle() {
      this.payVisible = true
      if (this.ids.length > 1) {
        this.$message.error('只能选择单条数据进行续费')
        return
      }
      if(this.ids[0].status == ''){

      }
      this.$nextTick(() => {
        this.$refs.pay.init(this.ids[0].carId)
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
