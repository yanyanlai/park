<template>
  <el-dialog
    :title="'续费'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="50%"
  >
    <el-form ref="dataForm" :model="dataForm" :rules="dataRule" label-width="100px" style="width:95%;" @keyup.enter.native="dataFormSubmit()">
      <el-row>
        <el-col :span="24">
          <el-form-item label="公司名称" prop="propertyId">
            <el-select v-model="dataForm.propertyId" placeholder="请选择公司" filterable style="width: 100%" disabled>
              <el-option v-for="park in propertyList" :key="park.propertyId" :label="park.propertyName" :value="park.propertyId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="停车场名称" prop="parkId">
            <el-select v-model="dataForm.parkId" placeholder="请选择停车场" filterable style="width: 100%" disabled>
              <el-option v-for="park in parkList" :key="park.parkId" :label="park.parkName" :value="park.parkId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="车牌号" prop="number">
            <el-input v-model="dataForm.number" placeholder="请输入车牌号" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="车主姓名" prop="name">
            <el-input v-model="dataForm.name" placeholder="请输入车主姓名" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="手机号码" prop="phone">
            <el-input v-model="dataForm.phone" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="状态" prop="status">
            <el-select v-model="dataForm.status" placeholder="请选择状态" filterable style="width: 100%" disabled>
              <el-option key="1" label="正常" :value="1" />
              <el-option key="2" label="欠费" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="类型" prop="type">
            <el-select v-model="dataForm.type" placeholder="请选择类型" filterable style="width: 100%" disabled>
              <el-option key="1" label="包月车" :value="1" />
              <el-option key="2" label="包年车" :value="2" />
              <el-option key="3" label="VIP车" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="车位" prop="space">
            <el-input v-model="dataForm.space" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="时间" prop="remark">
            <el-select v-model="dataForm.pay" placeholder="请选择类型" filterable style="width: 100%">
              <el-option v-for="pay in payList" :key="pay.value" :label="pay.label" :value="pay.value" />
            </el-select>
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
import { getPayInfo,getInfo, add, edit,updatePay } from '@/api/sys/car'
export default {
  data() {
    return {
      visible: false,
      propertyList: '',
      parkList: '',
      payList: '',
      dataForm: {
        carId: '',
        propertyId: '',
        parkId: '',
        number: '',
        name: '',
        gender: '男',
        phone: '',
        status: 1,
        type: 1,
        space: '',
        pay: ''
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.carId = id
      this.visible = true
      this.resetForm('dataForm')
      getPayInfo(id).then(res => {
        if (res && res.code === 200) {
          this.propertyList = res.data.propertyList
          this.parkList = res.data.parkList
          this.payList = res.data.payList
          if (id > 0) {
            this.dataForm.carId = res.data.car.carId
            this.dataForm.propertyId = res.data.car.propertyId
            this.dataForm.parkId = res.data.car.parkId
            this.dataForm.number = res.data.car.number
            this.dataForm.name = res.data.car.name
            this.dataForm.gender = res.data.car.gender
            this.dataForm.phone = res.data.car.phone
            this.dataForm.status = res.data.car.status
            this.dataForm.type = res.data.car.type
            this.dataForm.space = res.data.car.space
            this.dataForm.pay = this.payList[0].value
          } else {
            this.dataForm.propertyId = this.propertyList[0].propertyId
            this.dataForm.parkId = this.parkList[0].parkId
            this.dataForm.pay = this.payList[0].value
          }
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const param = {
            'carId': this.dataForm.carId,
            'month': this.dataForm.pay
          }
          updatePay(param).then(res => {
            if (res.code === 200) {
              this.visible = false
              this.$emit('refreshDataList')
              this.$message.success(res.msg)
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>
