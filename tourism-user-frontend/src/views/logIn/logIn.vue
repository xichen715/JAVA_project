<template>
  <div class="login-bg">
    <div class="login-center">
      <div class="login-box">
        <div class="login-logo-row">
          <img src="../../assets/image/logo.png" class="logo-img" alt="logo" />
          <span class="login-title">欢迎来到JoyPath乐途</span>
        </div>
        <el-input
            prefix-icon="el-icon-user"
            v-model="loginAccount"
            placeholder="请输入登录账号"
            class="login-input"
        ></el-input>
        <el-input
            prefix-icon="el-icon-star-off"
            type="password"
            v-model="password"
            placeholder="请输入用户密码"
            class="login-input"
        ></el-input>
        <div class="login-forget" @click="toForget">
          忘记密码？
        </div>
        <el-button class="login-btn" type="primary" @click="login" round>
          登 录
        </el-button>
        <el-button class="register-btn" @click="toRegister" round>
          注 册
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { login, getUser } from "../../api/api";
export default {
  data() {
    return {
      loginAccount: "",
      password: "",
    };
  },
  methods: {
    toRegister() {
      this.$router.push("/register");
    },
    toForget() {
      this.$router.push("/forget");
    },
    login() {
      if (!this.loginAccount) {
        this.$message({
          message: "请输入用户名",
          type: "warning",
        });
        return;
      }
      if (!this.password) {
        this.$message({
          message: "请输入密码",
          type: "warning",
        });
        return;
      }
      const params = {
        loginAccount: this.loginAccount,
        password: this.password,
      };
      login(params).then((res) => {
        if (res.code == 1000) {
          this.$message({
            message: "登录成功！",
            type: "success",
          });
          const token = res.data.token;
          window.localStorage.setItem("user_token", token);
          this.getUserInfo();
          setTimeout(() => {
            this.$router.push("/");
          }, 500);
        } else {
          this.$message.error(res.message);
        }
      });
    },
    getUserInfo() {
      getUser().then((res) => {
        if (res.code == 1000) {
          window.localStorage.setItem("user_info", JSON.stringify(res.data));
        }
      });
    },
  },
};
</script>

<style scoped>
.login-bg {
  width: 100vw;
  height: 100vh;
  min-height: 500px;
  font-family: 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  background: url('../../assets/image/image2.png') no-repeat center center;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.login-bg::before {
  content: '';
  position: absolute;
  left: 0; top: 0; width: 100%; height: 100%;
  background: rgba(255,255,255,0.12);
  z-index: 0;
}
.login-center {
  width: 100vw;
  height: 100vh;
  min-height: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 1;
}
.login-box {
  width: 410px;
  min-height: 410px;
  background: rgba(255,255,255,0.93);
  box-shadow: 0 8px 32px 0 rgba(62,120,243,0.10);
  border-radius: 18px;
  padding: 38px 32px 26px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
  /* backdrop-filter: blur(6px);  // 如果你想要毛玻璃效果可以打开 */
}
.login-logo-row {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24px;
  gap: 18px;
}
.logo-img {
  width: 80px;
  height: 80px;
}
.login-title {
  font-size: 28px;
  font-weight: bold;
  color: #d37136;
  letter-spacing: 2px;
}
.login-input {
  width: 100%;
  margin-bottom: 15px;
}
.login-forget {
  width: 100%;
  text-align: right;
  cursor: pointer;
  color: #3E78F3;
  font-size: 13px;
  margin-bottom: 18px;
  transition: color 0.2s;
}
.login-forget:hover {
  text-decoration: underline;
  color: #2863e6;
}
.login-btn {
  width: 100%;
  height: 44px;
  margin-bottom: 12px;
  font-size: 18px;
  font-weight: bold;
  letter-spacing: 1.5px;
  background: linear-gradient(90deg, #3E78F3 60%, #55adff 100%);
  border: none;
}
.register-btn {
  width: 100%;
  height: 44px;
  font-size: 17px;
  color: #3E78F3;
  background: #fff;
  border: 1.5px solid #3E78F3;
  font-weight: bold;
  letter-spacing: 1.5px;
  margin-bottom: 0;
  transition: background 0.2s, color 0.2s;
}
.register-btn:hover {
  background: #e4edff;
  color: #2863e6;
}
@media (max-width: 900px) {
  .login-box { width: 90vw; max-width: 400px; }
}
</style>