<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="标题内容：">
        <el-input v-model="queryParam.name" clearable></el-input>
      </el-form-item>
      <el-form-item label="学科：" >
        <el-select v-model="queryParam.subjectId"  clearable>
          <el-option v-for="item in subjectFilter" :key="item.id" :value="item.id" :label="item.name+' ( '+item.levelName+' )'"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">查询</el-button>
        <router-link :to="{path:'/education/textbook/edit'}" class="link-left">
          <el-button type="primary">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>
    <el-table v-loading="listLoading" :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column prop="id" label="Id" width="90px"/>
      <el-table-column prop="subjectId" label="学科" :formatter="subjectFormatter" width="120px" />
      <el-table-column prop="name" label="名称"  />
      <el-table-column  label="预览" align="center"  width="160px">
        <template slot-scope="{row}">
          <el-button size="mini" @click="$router.push({path:'/threedviewer',query:{filePath:row.filePath}})" >打开页面</el-button>
        </template>
      </el-table-column>
      <el-table-column  label="操作" align="center"  width="160px">
        <template slot-scope="{row}">
          <el-button size="mini" @click="$router.push({path:'/education/textbook/edit',query:{id:row.id}})" >编辑</el-button>
          <el-button size="mini" type="danger"  @click="deleteRow(row)" class="link-left">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParam.pageIndex" :limit.sync="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>

<script>
import { mapGetters, mapState, mapActions } from 'vuex'
import Pagination from '@/components/Pagination'
import textBookApi from '@/api/textBook'

export default {
  components: { Pagination },
  data () {
    return {
      queryParam: {
        id: null,
        name: null,
        subjectId: null,
        filePath: null,
        pageIndex: 1,
        pageSize: 10
      },
      subjectFilter: null,
      listLoading: true,
      tableData: [],
      total: 0
    }
  },
  created () {
    this.initSubject()
    this.search()
  },
  methods: {
    submitForm () {
      this.queryParam.pageIndex = 1
      this.search()
    },
    search () {
      this.listLoading = true
      textBookApi.pageList(this.queryParam).then(data => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = re.pageNum
        this.listLoading = false
      })
    },
    deleteRow (row) {
      let _this = this
      textBookApi.deleteTextBook(row.id).then(re => {
        if (re.code === 1) {
          _this.search()
          _this.$message.success(re.message)
        } else {
          _this.$message.error(re.message)
        }
      })
    },
    subjectFormatter  (row, column, cellValue, index) {
      return this.subjectEnumFormat(cellValue)
    },
    ...mapActions('exam', { initSubject: 'initSubject' })
  },
  computed: {
    ...mapGetters('enumItem', ['enumFormat']),
    ...mapState('enumItem', {
      levelEnum: state => state.user.levelEnum
    }),
    ...mapGetters('exam', ['subjectEnumFormat']),
    ...mapState('exam', { subjects: state => state.subjects })
  }
}
</script>
