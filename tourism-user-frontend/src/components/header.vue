<template>
  <div class="header">
    <div class="header-inner">
      <div class="header-logo">
        <img src="../assets/image/logo.png" alt="logo" />
      </div>
      <div class="header-menu">
        <div
            v-for="item in menuList"
            :key="item.name"
            @click="toPage(item.name)"
            :class="['menu-item', $route.path === item.path ? 'active' : '']"
        >
          {{ item.label }}
        </div>
      </div>
      <div class="header-actions">
        <div class="user-info" @click="toCenter">
          <el-avatar
              :src="userAvatar"
              size="small"
              class="avatar"
          />
          <span class="username">{{ userName }}</span>
        </div>
        <div class="action-btn" @click="loginOut">退出</div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
export default {
  data() {
    return {
      menuList: [
        { name: "index", label: "主页", path: "/" },
        { name: "attractions", label: "景点", path: "/attractions" },
        { name: "line", label: "行程规划", path: "/line" },
        { name: "hotel", label: "订酒店", path: "/hotel" },
        { name: "forum", label: "游记", path: "/forum" },
        { name: "favor", label: "我的收藏", path: "/favor" },
        { name: "order", label: "我的预定", path: "/order" },
        { name: "manage", label: "后台管理", path: "/manage" }
      ]
    };
  },
  computed: {
    ...mapState(['HOST', 'userInfo']),
    userName() {
      return this.userInfo?.userName || "未登录";
    },
    userAvatar() {
      if (this.userInfo?.avatar) {
        // 每次渲染都带上时间戳，防止缓存
        return this.HOST + this.userInfo.avatar + '?t=' + Date.now();
      }
      return require('@/assets/image/image2.png');
    }
  },
  methods: {
    toCenter() {
      this.$router.push("/center");
    },
    toPage(name) {
      if (name === "manage") {
        window.open("http://localhost:3000");
      } else {
        const item = this.menuList.find(i => i.name === name);
        if (item) this.$router.push(item.path);
      }
    },
    loginOut() {
      this.$store.dispatch("logout").then(() => {
        this.$message({
          message: "退出账号成功！",
          type: "success"
        });
        setTimeout(() => {
          window.location.reload();
        }, 1000);
      });
    }
  }
};
</script>

<style scoped>
.header {
  width: 100%;
  height: 80px;
  background: #fff;
  display: flex;
  align-items: center;
  box-shadow: 0 1px 4px rgba(0,0,0,.03);
}
.header-inner {
  width: 90%;
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 80px;
}
.header-logo {
  flex: 0 0 auto;
  display: flex;
  align-items: center;
  height: 100%;
  margin-right: 32px;
}
.header-logo img {
  height: 76px;
  display: block;
}
.header-menu {
  flex: 1 1 auto;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 24px;
}
.menu-item {
  font-size: 18px;
  color: #000;
  cursor: pointer;
  padding: 0 8px;
  border-radius: 4px;
  transition: background .2s,color .2s;
  line-height: 80px;
}
.menu-item.active {
  color: #DC1616;
  font-weight: bold;
  background: #f8f8f8;
}
.header-actions {
  flex: 0 0 auto;
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: 32px;
}
.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0 16px 0 0;
  border-radius: 18px;
  transition: background 0.2s;
  margin-right: 4px;
}
.user-info:hover {
  background: #f5f7fa;
}
.avatar {
  margin-right: 8px;
  background: #c0c4cc;
}
.username {
  font-size: 16px;
  color: #3571d8;
  font-weight: 500;
  max-width: 120px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
.action-btn {
  width: 90px;
  height: 36px;
  border-radius: 18px;
  color: #fff;
  background: #4381FF;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 15px;
  cursor: pointer;
  transition: background .2s;
}
.action-btn:hover {
  background: #2d5bbf;
}
</style>