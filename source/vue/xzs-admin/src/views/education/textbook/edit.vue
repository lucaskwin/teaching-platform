<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
      <el-form-item label="名称：" required>
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="学科：" prop="subjectId" required>
        <el-select v-model="form.subjectId" placeholder="学科">
          <el-option v-for="item in subjectFilter" :key="item.id" :value="item.id"
                     :label="item.name+' ( '+item.levelName+' )'"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文件：" required>
        <el-upload
          class="upload-demo"
          :action="uploadUrl"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :before-remove="beforeRemove"
          multiple
          :limit="3"
          :on-exceed="handleExceed"
          :on-success="uploadSuccess"
          :file-list="form.filePathItems"
          :show-file-list="true"
        >

          <el-button size="small" type="primary">点击上传</el-button>
<!--          <div slot="tip" class="el-upload__tip">支持上传obj,json,fbx,stl,dae,ply,ifc文件</div>-->
        </el-upload>
        <el-input :value="JSON.stringify(form.filePathItems)"></el-input>

      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import { mapGetters, mapState, mapActions } from 'vuex'
import textBookApi from '@/api/textBook'

export default {
  data () {
    return {
      uploadUrl: '/api/admin/upload/image',
      form: {
        id: null,
        name: '',
        subjectId: null,
        filePathItems: [{name:"logo.png",url:"http://se8eod8ng.hn-bkt.clouddn.com/FjkZRhnDInDB4SifDOkDqQcKj2CW"}]
      },
      subjectFilter: null,
      formLoading: false,
      rules: {
        subjectId: [
          { required: true, message: '请选择学科', trigger: 'change' }
        ],
        name: [
          { required: true, message: '请输入文件名称', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    let id = this.$route.query.id
    let _this = this
    this.initSubject(function () {
      _this.subjectFilter = _this.subjects
    })
    if (id && parseInt(id) !== 0) {
      _this.formLoading = true
      textBookApi.select(id).then(re => {
        _this.form = re.response
        _this.formLoading = false
      })
    }
  },
  methods: {
    handleRemove(file, fileList) {
      // console.log(file, fileList);
      this.form.filePathItems = fileList
    },
    handlePreview(file) {
      // console.log(file);
    },
    handleExceed(files, fileList) {
      // 超过文件个数限制时的钩子
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    uploadSuccess(re, file, fileList) {
      // 成功上传文件时的钩子
      console.log(re)
      console.log(file)
      const uploadedFile = fileList.find(item => item.uid === file.uid);

      // 更新该文件对象的 url 属性
      if (uploadedFile) {
        uploadedFile.url = re.response;
      }
      this.form.filePathItems = fileList
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    submitForm () {
      let _this = this
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          textBookApi.edit(this.form).then(re => {
            if (re.code === 1) {
              _this.$message.success(re.message)
              _this.delCurrentView(_this).then(() => {
                _this.$router.push('/education/textbook/list')
              })
            } else {
              _this.$message.error(re.message)
              this.formLoading = false
            }
          }).catch(e => {
            this.formLoading = false
          })
        } else {
          return false
        }
      })
    },
    subjectFormatter (row, column, cellValue, index) {
      return this.subjectEnumFormat(cellValue)
    },
    resetForm () {
      let lastId = this.form.id
      this.$refs['form'].resetFields()
      this.form = {
        id: null,
        subjectId: null,
        name: '',
        filePathItems: null
      }
      this.form.id = lastId
    },
    ...mapActions('exam', { initSubject: 'initSubject' })
  },
  computed: {
    ...mapGetters('enumItem', ['enumFormat']),
    ...mapState('enumItem', {
      questionTypeEnum: state => state.exam.question.typeEnum,
      paperTypeEnum: state => state.exam.examPaper.paperTypeEnum,
      levelEnum: state => state.user.levelEnum
    }),
    ...mapState('exam', { subjects: state => state.subjects })
  }
}
</script>

<style lang="scss">
</style>
