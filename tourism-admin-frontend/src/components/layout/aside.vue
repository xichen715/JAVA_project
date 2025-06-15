<template>
  <el-aside :width="isCollapse ? '64px' : '240px'">
    <div class="logo-container" :style="{ width: isCollapse ? '64px' : '240px' }">
      <img class="logo" src="../../assets/image/logo.png" />
      <transition name="fade">
        <span v-if="!isCollapse" class="logo-text">JoyPath</span>
      </transition>
    </div>
    <el-menu
        :default-active="$route.path + '@' + $route.name"
        class="el-menu-vertical"
        :collapse="isCollapse"
        :collapse-transition="false"
        background-color="#222B40"
        :text-color="textColor"
        :unique-opened="true"
        @select="handleSelect"
    >
      <el-menu-item index="/index@首页">
        <i class="el-icon-menu" />
        <span slot="title">首页</span>
      </el-menu-item>
      <el-menu-item index="/attractions@景点管理">
        <i class="el-icon-s-ticket" />
        <span slot="title">景点管理</span>
      </el-menu-item>
      <el-menu-item index="/line@线路管理">
        <i class="el-icon-help" />
        <span slot="title">线路管理</span>
      </el-menu-item>
      <el-menu-item index="/hotel@酒店管理">
        <i class="el-icon-s-shop" />
        <span slot="title">酒店管理</span>
      </el-menu-item>
      <el-menu-item index="/forum@资讯管理">
        <i class="el-icon-picture-outline-round" />
        <span slot="title">资讯管理</span>
      </el-menu-item>
      <el-menu-item index="/order@景点预约">
        <i class="el-icon-s-promotion" />
        <span slot="title">景点预约</span>
      </el-menu-item>
      <el-menu-item index="/orderHotel@酒店预约">
        <i class="el-icon-s-home" />
        <span slot="title">酒店预约</span>
      </el-menu-item>
      <el-menu-item index="/rotations@轮播图管理">
        <i class="el-icon-picture" />
        <span slot="title">轮播图管理</span>
      </el-menu-item>
      <el-menu-item index="/center@个人中心">
        <i class="el-icon-user" />
        <span slot="title">个人中心</span>
      </el-menu-item>
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-s-platform" />
          <span v-if="!isCollapse">用户管理</span>
        </template>
        <el-menu-item index="/admin@管理员管理">
          <i class="el-icon-s-promotion" />
          <span slot="title">管理员管理</span>
        </el-menu-item>
        <el-menu-item index="/user@普通用户管理">
          <i class="el-icon-coordinate" />
          <span slot="title">普通用户管理</span>
        </el-menu-item>
      </el-submenu>
    </el-menu>
  </el-aside>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: "Asside",
  data() {
    return {
      isCollapse: false,
      textColor: "#A4B7CC"
    }
  },
  computed: {
    ...mapState({
      activeMenuArrary: state => state.menu.activeMenuArrary,
      displayMenus: state => state.menu.displayMenus
    })
  },
  methods: {
    handleSelect(key) {
      const [path, name] = key.split('@')
      this.$router.push({ path })
      this.$store.commit('menu/setActiveMenu', path)

      if (!this.activeMenuArrary.some(item => item.url === path)) {
        this.$store.commit('menu/addActiveMenu', { name, url: path })
      }
    }
  },
  mounted() {
    this.$bus.$on('collapse', res => {
      this.isCollapse = res
    })
    const { path, name } = this.$route
    this.$store.commit('menu/setActiveMenu', path)

    if (!this.activeMenuArrary.some(item => item.url === path)) {
      this.$store.commit('menu/addActiveMenu', { name, url: path })
    }
  }
}
</script>

<style scoped lang="scss">
.el-aside {
  background-color: #222B40;
  transition: width 0.3s ease;
}

.logo-container {
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #222B40;
  transition: width 0.3s ease;
  overflow: hidden;
}

.logo {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.logo-text {
  color: #fff;
  font-family: 'Mulish', sans-serif;
  font-weight: bold;
  font-size: 16px;
  margin-left: 10px;
  white-space: nowrap;
}

.el-menu {
  height: calc(100vh - 100px);
  font-family: '黑体', sans-serif;
  font-weight: 550;
  overflow-x: hidden;
  border-right: none;
}

.el-menu::-webkit-scrollbar,
.el-aside::-webkit-scrollbar {
  width: 0;
  height: 0;
}

.el-menu-item,
.el-submenu__title {
  height: 47px !important;
  line-height: 47px !important;
  transition: background-color 0.3s ease;
}

.el-submenu >>> .el-submenu__title:hover {
  color: #EAB040 !important;
  background-color: #2F3950 !important;
}

.el-menu-vertical:deep(.el-menu-item:hover) {
  color: #49b1f5 !important;
  background-color: #1d2b45 !important;
}

.el-menu-vertical:deep(.el-menu-item.is-active) {
  background-color: #1d2b45 !important;
  box-shadow: inset 5px 0 #49b1f5;
  color: #fff !important;
}

.el-submenu .el-menu-item {
  background-color: #1B2233 !important;
}
</style>
