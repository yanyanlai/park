<template>
  <el-dialog
    :title="!dataForm.propertyId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="50%"
  >
    <el-form ref="dataForm" :model="dataForm" :rules="dataRule" label-width="100px" style="width:95%;" @keyup.enter.native="dataFormSubmit()">
      <el-row>
        <el-col :span="24">
          <el-form-item label="公司编号" prop="propertyCode">
            <el-input v-model="dataForm.propertyCode" placeholder="请输入公司编号" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="公司名称" prop="propertyName">
            <el-input v-model="dataForm.propertyName" placeholder="请输入公司名称" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="联系人" prop="contact">
            <el-input v-model="dataForm.contact" placeholder="请输入联系人" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="dataForm.phone" placeholder="请输入联系电话" />
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
import { getInfo, add, edit } from '@/api/sys/property'
export default {
  data() {
    return {
      visible: false,
      dataForm: {
        propertyId: '',
        propertyCode: '',
        propertyName: '',
        contact: '',
        phone: ''
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
      this.dataForm.propertyId = id
      this.visible = true
      this.resetForm('dataForm')
      // this.dataForm.seq = 1
      if (this.dataForm.propertyId) {
        getInfo(id).then(res => {
          if (res && res.code === 200) {
            this.dataForm.propertyId = res.data.propertyId
            this.dataForm.propertyCode = res.data.propertyCode
            this.dataForm.propertyName = res.data.propertyName
            this.dataForm.contact = res.data.contact
            this.dataForm.phone = res.data.phone
          }
        })
      }
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const param = {
            'propertyId': this.dataForm.propertyId,
            'propertyCode': this.dataForm.propertyCode,
            'propertyName': this.dataForm.propertyName,
            'contact': this.dataForm.contact,
            'phone': this.dataForm.phone
          }
          if (!this.dataForm.propertyId) {
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
