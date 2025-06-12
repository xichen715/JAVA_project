<template>
  <div class="login-bg">
    <div class="login-center">
      <div class="login-box">
        <div class="login-logo-row">
          <img src="../../assets/image/logo.png" class="logo-img" alt="logo" />
          <span class="login-title">欢迎来到JoyPath乐途</span>
        </div>
        <el-input prefix-icon="el-icon-user" v-model="loginAccount" placeholder="请输入登录账号" class="login-input"></el-input>
        <el-input prefix-icon="el-icon-star-off" type="password" v-model="password" placeholder="请输入新密码" class="login-input"></el-input>
        <el-input prefix-icon="el-icon-s-order" v-model="email" placeholder="请输入邮箱" class="login-input"></el-input>
        <div class="input-with-btn">
          <el-input prefix-icon="el-icon-s-ticket" v-model="code" placeholder="请输入验证码" class="login-input code-input"></el-input>
          <el-button class="code-btn" @click="getEmailReg" round>获取验证码</el-button>
        </div>
        <el-button class="action-btn submit-btn" type="primary" @click="forgetPassword" round>
          提 交
        </el-button>
        <el-button class="action-btn login-btn" plain @click="toLogin" round>
          返回登录
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getEmailReg, forgetPassword } from '../../api/api'
export default {
  data() {
    return {
      loginAccount: "",
      password: "",
      email: "",
      code: "",
    }
  },
  methods: {
    toLogin() {
      this.$router.push("/login")
    },
    getEmailReg() {
      if (!this.email) {
        this.$message({ message: '请输入邮箱', type: 'warning' });
        return;
      }
      var regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if (!regex.test(this.email)) {
        this.$message({ message: '请输入正确的邮箱格式', type: 'warning' });
        return;
      }
      getEmailReg({ email: this.email }).then(res => {
        if (res.code == 100) {
          this.$message({ message: '验证码已发送', type: 'success' });
        } else {
          this.$message({ message: res.message, type: 'warning' });
        }
      })
    },
    forgetPassword() {
      if (!this.loginAccount) {
        this.$message({ message: '请输入登录账号', type: 'warning' });
        return;
      }
      if (!this.password) {
        this.$message({ message: '请输入密码', type: 'warning' });
        return;
      }
      if (!this.code) {
        this.$message({ message: '请输入验证码', type: 'warning' });
        return;
      }
      if (!this.email) {
        this.$message({ message: '请输入邮箱', type: 'warning' });
        return;
      }
      var regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if (!regex.test(this.email)) {
        this.$message({ message: '请输入正确的邮箱格式', type: 'warning' });
        return;
      }
      var param = {
        loginAccount: this.loginAccount,
        password: this.password,
        email: this.email,
        code: this.code
      }
      forgetPassword(param).then(res => {
        if (res.code == 1000) {
          this.$message({ message: '密码修改成功', type: 'success' });
          setTimeout(() => {
            this.$router.push("/login")
          }, 500)
        } else {
          this.$message({ message: res.message, type: 'warning' });
        }
      })
    }
  }
}
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
  background: rgba(255,255,255,0.96);
  box-shadow: 0 8px 32px 0 rgba(62,120,243,0.10);
  border-radius: 18px;
  padding: 38px 32px 26px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
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
.input-with-btn {
  width: 100%;
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}
.code-input {
  flex: 1;
  margin-bottom: 0;
  margin-right: 12px;
}
.code-btn {
  min-width: 110px;
  font-size: 15px;
  background: #e4edff;
  color: #3E78F3;
  border: 1.5px solid #3E78F3;
  border-radius: 22px;
  font-weight: bold;
}
.action-btn {
  width: 100%;
  height: 48px;
  font-size: 18px;
  font-weight: bold;
  letter-spacing: 2px;
  border-radius: 24px;
  margin-bottom: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s, color 0.2s;
  box-sizing: border-box;
}
.submit-btn {
  background: linear-gradient(90deg, #3E78F3 60%, #55adff 100%);
  color: #fff;
  border: none;
}
.login-btn {
  background: #fff;
  color: #3E78F3;
  border: 1.5px solid #3E78F3;
  margin-bottom: 0;
}
.login-btn:hover {
  background: #e4edff;
  color: #2863e6;
}
@media (max-width: 900px) {
  .login-box { width: 90vw; max-width: 400px; }
}
</style>