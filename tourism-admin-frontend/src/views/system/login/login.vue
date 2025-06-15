<template>
  <div class="login-container">
    <div class="login-left"></div>

    <div class="login-right">
      <div class="login-box">
        <div class="login-header">
          <img src="@/assets/image/logo.png" alt="logo" class="logo" />
          <h2 class="brand">Joypath乐途</h2>
        </div>

        <p class="slogan">旅行让生活变得更加有意义！</p>

        <el-input
            prefix-icon="el-icon-user"
            v-model="loginAccount"
            placeholder="请输入登录账号"
            class="login-input"
        ></el-input>

        <el-input
            prefix-icon="el-icon-lock"
            type="password"
            v-model="password"
            placeholder="请输入用户密码"
            class="login-input"
        ></el-input>

        <el-button
            type="primary"
            class="login-button"
            round
            @click="login"
            :loading="loading"
        >
          登 录
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { login, getUser } from '../../../api/api'

export default {
  data() {
    return {
      loginAccount: '',
      password: '',
      loading: false
    }
  },
  methods: {
    login() {
      if (!this.loginAccount) {
        this.$message.warning('请输入用户名');
        return;
      }
      if (!this.password) {
        this.$message.warning('请输入密码');
        return;
      }

      this.loading = true;
      const params = {
        loginAccount: this.loginAccount,
        password: this.password
      };

      login(params).then(res => {
        this.loading = false;
        if (res.code === 1000) {
          this.$message.success('登录成功');
          this.$store.commit('user/setToken', res.data.token);
          this.getUserInfo();
          setTimeout(() => {
            this.$router.push('/index');
          }, 500);
        } else {
          this.$message.error(res.message || '登录失败');
        }
      }).catch(() => {
        this.loading = false;
        this.$message.error('网络错误，请稍后重试');
      });
    },
    getUserInfo() {
      getUser().then(res => {
        if (res.code === 1000) {
          this.$store.commit('user/setUser', JSON.stringify(res.data));
        }
      });
    }
  }
};
</script>

<style scoped>
:root {
  --primary-color: #3E78F3;
  --bg-color: #f5f7fa;
  --font-color: #333;
}

.login-container {
  display: flex;
  height: 100vh;
  width: 100%;
  font-family: 'Helvetica Neue', '黑体', sans-serif;
  background-color: var(--bg-color);
}

.login-left {
  flex: 3;
  background: url('../../../assets/image/image 2.png') no-repeat center center;
  background-size: cover;
}

.login-right {
  flex: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
}

.login-box {
  width: 80%;
  max-width: 360px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.logo {
  width: 50px;
  margin-right: 10px;
}

.brand {
  font-size: 26px;
  font-weight: bold;
  color: var(--font-color);
}

.slogan {
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
  text-align: center;
}

.login-input {
  width: 100%;
  margin-bottom: 20px;
}

.login-button {
  width: 100%;
  height: 45px;
  font-size: 16px;
  border-color: var(--primary-color);
}

.login-button:hover {
  background-color: #2e66dc;
  border-color: #2e66dc;
}
</style>
