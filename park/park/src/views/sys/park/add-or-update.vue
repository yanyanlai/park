<template>
  <el-dialog
    :title="!dataForm.parkId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="50%"
  >
    <el-form ref="dataForm" :model="dataForm" :rules="dataRule" label-width="100px" style="width:95%;" @keyup.enter.native="dataFormSubmit()">
      <el-row>
        <el-col :span="24">
          <el-form-item label="公司名称" prop="propertyId">
            <el-select v-model="dataForm.propertyId" placeholder="请选择单位" filterable style="width: 100%">
              <el-option v-for="park in propertyList" :key="park.propertyId" :label="park.propertyName" :value="park.propertyId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="停车场名称" prop="parkName">
            <el-input v-model="dataForm.parkName" placeholder="请输入停车场名称" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="车位总数" prop="parkCount">
            <el-input v-model="dataForm.parkCount" placeholder="请输入车位总数" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="包月车辆" prop="monthlyCount">
            <el-input v-model="dataForm.monthlyCount" placeholder="请输入包月车辆" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="包月费用" prop="monthlyPrice">
            <el-input v-model="dataForm.monthlyPrice" placeholder="请输入包月费用" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="免费时长" prop="freeDuration">
            <el-select v-model="dataForm.freeDuration" placeholder="请选择免费时长" filterable style="width: 100%">
              <el-option label="15" value="15" />
              <el-option label="30" value="30" />
              <el-option label="45" value="45" />
              <el-option label="60" value="60" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="计费单位" prop="chargeUnit">
            <el-input v-model="dataForm.chargeUnit" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="计费单价" prop="chargeUnit">
            <el-input v-model="dataForm.chargePrice" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="最大金额" prop="chargeUnit">
            <el-input v-model="dataForm.maxCharge" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="经度" prop="lng">
            <el-input v-model="dataForm.lng" placeholder="请输入经度" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="纬度" prop="lat">
            <el-input v-model="dataForm.lat" placeholder="请输入维度" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="danger" @click="visible = false">取 消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { getInfo, add, edit } from '@/api/sys/park'
export default {
  data() {
    return {
      visible: false,
      propertyList: '',
      dataForm: {
        parkId: '',
        propertyId: '',
        parkName: '',
        parkCount: '',
        monthlyCount: '',
        monthlyPrice: '',
        freeDuration: '',
        chargeUnit: 60,
        chargePrice: '',
        maxCharge: '',
        lng: '',
        lat: ''
      },
      dataRule: {
        propertyCode: [
          { required: true, message: '公司编号不能为空', trigger: 'blur' }
        ],
        propertyName: [
          { required: true, message: '公司名称不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.parkId = id
      this.visible = true
      this.resetForm('dataForm')
      getInfo(id).then(res => {
        if (res && res.code === 200) {
          this.propertyList = res.data.propertyList
          if (id > 0) {
            this.dataForm.parkId = res.data.park.parkId
            this.dataForm.propertyId = res.data.park.propertyId
            this.dataForm.parkName = res.data.park.parkName
            this.dataForm.parkCount = res.data.park.parkCount
            this.dataForm.monthlyCount = res.data.park.monthlyCount
            this.dataForm.monthlyPrice = res.data.park.monthlyPrice
            this.dataForm.freeDuration = res.data.park.freeDuration
            this.dataForm.chargeUnit = res.data.park.chargeUnit
            this.dataForm.chargePrice = res.data.park.chargePrice
            this.dataForm.maxCharge = res.data.park.maxCharge
            this.dataForm.lng = res.data.park.lng
            this.dataForm.lat = res.data.park.lat
          } else {
            this.dataForm.propertyId = this.propertyList[0].propertyId
          }
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const param = {
            'parkId': this.dataForm.parkId,
            'propertyId': this.dataForm.propertyId,
            'parkName': this.dataForm.parkName,
            'parkCount': this.dataForm.parkCount,
            'monthlyCount': this.dataForm.monthlyCount,
            'monthlyPrice': this.dataForm.monthlyPrice,
            'freeDuration': this.dataForm.freeDuration,
            'chargeUnit': this.dataForm.chargeUnit,
            'chargePrice': this.dataForm.chargePrice,
            'maxCharge': this.dataForm.maxCharge,
            'lng': this.dataForm.lng,
            'lat': this.dataForm.lat
          }
          if (!this.dataForm.parkId) {
            add(param).then(res => {
              if (res.code === 200) {
                this.visible = false
                this.$emit('refreshDataList')
                this.$message.success(res.msg)
              } else {
                this.$message.error(res.msg)
              }
            })
          } else {
            edit(param).then(res => {
              if (res.code === 200) {
                this.visible = false
                this.$emit('refreshDataList')
                this.$message.success(res.msg)
              } else {
                this.$message.error(res.msg)
              }
            })
          }
        }
      })
    }
  }
}
</script>
