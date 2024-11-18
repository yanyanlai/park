<template>
  <el-dialog
    :title="!dataForm.carId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="50%"
  >
    <el-form ref="dataForm" :model="dataForm" :rules="dataRule" label-width="100px" style="width:95%;" @keyup.enter.native="dataFormSubmit()">
      <el-row>
        <el-col :span="24">
          <el-form-item label="公司名称" prop="propertyId">
            <el-select v-model="dataForm.propertyId" placeholder="请选择公司" filterable style="width: 100%">
              <el-option v-for="park in propertyList" :key="park.propertyId" :label="park.propertyName" :value="park.propertyId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="停车场名称" prop="parkId">
            <el-select v-model="dataForm.parkId" placeholder="请选择停车场" filterable style="width: 100%">
              <el-option v-for="park in parkList" :key="park.parkId" :label="park.parkName" :value="park.parkId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="车牌号" prop="number">
            <el-input v-model="dataForm.number" placeholder="请输入车牌号" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="车主姓名" prop="name">
            <el-input v-model="dataForm.name" placeholder="请输入车主姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="dataForm.gender">
              <el-radio :label="'男'">男</el-radio>
              <el-radio :label="'女'">女</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="手机号码" prop="phone">
            <el-input v-model="dataForm.phone" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="车位" prop="space">
            <el-input v-model="dataForm.space" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="dataForm.remark" placeholder="请输入备注" />
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
import { getInfo, add, edit } from '@/api/sys/car'
export default {
  data() {
    return {
      visible: false,
      propertyList: '',
      parkList: '',
      dataForm: {
        carId: '',
        propertyId: '',
        parkId: '',
        number: '',
        name: '',
        gender: '男',
        phone: '',
        type: 1,
        space: '',
        remark: ''
      },
      dataRule: {
        number: [
          { required: true, message: '车牌号不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '车主姓名不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.carId = id
      this.visible = true
      this.resetForm('dataForm')
      getInfo(id).then(res => {
        if (res && res.code === 200) {
          this.propertyList = res.data.propertyList
          this.parkList = res.data.parkList
          if (id > 0) {
            this.dataForm.carId = res.data.car.carId
            this.dataForm.propertyId = res.data.car.propertyId
            this.dataForm.parkId = res.data.car.parkId
            this.dataForm.number = res.data.car.number
            this.dataForm.name = res.data.car.name
            this.dataForm.gender = res.data.car.gender
            this.dataForm.phone = res.data.car.phone
            this.dataForm.type = res.data.car.type
            this.dataForm.space = res.data.car.space
            this.dataForm.remark = res.data.car.remark
          } else {
            this.dataForm.propertyId = this.propertyList[0].propertyId
            this.dataForm.parkId = this.parkList[0].parkId
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
            'propertyId': this.dataForm.propertyId,
            'parkId': this.dataForm.parkId,
            'number': this.dataForm.number,
            'name': this.dataForm.name,
            'gender': this.dataForm.gender,
            'phone': this.dataForm.phone,
            'type': this.dataForm.type,
            'space': this.dataForm.space,
            'remark': this.dataForm.remark
          }
          if (!this.dataForm.carId) {
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
