<template>
  <div class="travel-note-list">
    <headers></headers>
    <div class="search-bar">
      <el-input
          size="small"
          class="search-input"
          v-model="search.title"
          placeholder="请输入游记标题"
          clearable
          @keyup.enter.native="searchPage"
      ></el-input>
      <el-button size="small" class="search-btn" type="primary" plain @click="searchPage">搜索</el-button>
      <el-button size="small" class="create-btn" type="primary" @click="goToCreate">发布游记</el-button>
    </div>
    <el-table
        :data="noteList"
        stripe
        border
        class="note-table"
        size="medium"
        empty-text="暂无游记"
        highlight-current-row
    >
      <el-table-column
          prop="title"
          label="标题"
          min-width="200"
          show-overflow-tooltip
      >
        <template slot-scope="scope">
          <el-link
              type="primary"
              @click="goToDetail(scope.row.id)"
              :underline="false"
              style="font-weight:bold"
          >
            {{ scope.row.title }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
          prop="userId"
          label="作者ID"
          width="160"
          show-overflow-tooltip
      />
      <el-table-column
          prop="tag"
          label="标签"
          width="120"
          show-overflow-tooltip
      >
        <template slot-scope="scope">
          <el-tag size="mini" v-if="scope.row.tag">{{ scope.row.tag }}</el-tag>
          <span v-else class="gray-text">无</span>
        </template>
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="创建时间"
          width="170"
      >
        <template slot-scope="scope">
          {{ formatDate(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column
          prop="viewCount"
          label="浏览量"
          width="90"
          align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-view" style="color:#909399"></i> {{ scope.row.viewCount }}
        </template>
      </el-table-column>
      <el-table-column
          prop="likeCount"
          label="点赞数"
          width="90"
          align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-thumb" style="color:#f56c6c"></i> {{ scope.row.likeCount }}
        </template>
      </el-table-column>
      <el-table-column
          label="操作"
          width="150"
          align="center"
          fixed="right"
      >
        <template slot-scope="scope">
          <el-button size="mini" @click="goToEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" type="danger" @click="delNote(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
          background
          layout="prev, pager, next, jumper"
          :page-size="search.pageSize"
          :total="total"
          :current-page="search.pageNumber"
          @current-change="handleCurrentChange"
          hide-on-single-page
      />
    </div>
  </div>
</template>

<script>
import { getTravelNoteList, deleteTravelNote } from "@/api/api";
import { MessageBox, Message } from 'element-ui';
import headers from "@/components/header.vue";

export default {
  name: 'TravelNoteList',
  components: { headers },
  data() {
    return {
      search: {
        title: "",
        pageNumber: 1,
        pageSize: 12,
      },
      total: 0,
      noteList: [],
    }
  },
  created() {
    this.getNoteList();
  },
  methods: {
    searchPage() {
      this.search.pageNumber = 1;
      this.getNoteList();
    },
    getNoteList() {
      getTravelNoteList(this.search).then(res => {
        if (res.code === 1000) {
          if (Array.isArray(res.data)) {
            this.noteList = res.data;
            this.total = res.data.length;
          } else if (res.data && Array.isArray(res.data.records)) {
            this.noteList = res.data.records;
            this.total = res.data.total;
          } else {
            this.noteList = [];
            this.total = 0;
          }
        } else {
          this.noteList = [];
          this.total = 0;
        }
      });
    },
    handleCurrentChange(val) {
      this.search.pageNumber = val;
      this.getNoteList();
    },
    goToDetail(id) {
      this.$router.push({ name: 'travelNoteDetail', params: { id } });
    },
    goToEdit(id) {
      this.$router.push({ name: 'travelNoteEdit', params: { id } });
    },
    goToCreate() {
      this.$router.push({ name: 'travelNoteEdit' });
    },
    delNote(id) {
      MessageBox.confirm('确定要删除这篇游记吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTravelNote(id).then(res => {
          if (res.code === 1000) {
            Message.success('删除成功');
            this.getNoteList();
          }
        });
      }).catch(() => { });
    },
    formatDate(val) {
      if (!val) return '';
      const d = new Date(val);
      const y = d.getFullYear();
      const m = (d.getMonth() + 1).toString().padStart(2, "0");
      const day = d.getDate().toString().padStart(2, "0");
      const h = d.getHours().toString().padStart(2, "0");
      const min = d.getMinutes().toString().padStart(2, "0");
      const s = d.getSeconds().toString().padStart(2, "0");
      return `${y}-${m}-${day} ${h}:${min}:${s}`;
    }
  }
}
</script>

<style scoped>
.travel-note-list {
  background: #f8f9fb;
  min-height: 100vh;
  padding: 0 0 40px 0;
}
.search-bar {
  display: flex;
  align-items: center;
  padding: 24px 0 16px 0;
  background: #fff;
  border-radius: 8px;
  margin-top: 24px;
  margin-bottom: 0;
  box-shadow: 0 2px 8px #f0f1f2;
}
.search-input {
  width: 320px;
  margin-left: 20px;
}
.search-btn {
  margin-left: 20px;
}
.create-btn {
  margin-left: auto;
  margin-right: 40px;
}
.note-table {
  margin-top: 10px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px #f0f1f2;
}
.gray-text {
  color: #bdbdbd;
  font-size: 13px;
}
.pagination {
  margin-top: 24px;
  text-align: center;
}
::v-deep .el-table__cell {
  padding: 9px 0;
}
::v-deep .el-table th {
  background: #f3f6fa;
  color: #444;
  font-weight: 600;
}
</style>