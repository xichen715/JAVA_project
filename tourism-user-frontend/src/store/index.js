import Vue from 'vue'
import Vuex from 'vuex'
import { logout, getUser } from '@/api/api'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    HOST: 'http://localhost:8080',
    userInfo: JSON.parse(window.localStorage.getItem('user_info') || '{}')
  },
  getters: {
    // 可选：快速获取用户名和头像
    userName: state => state.userInfo?.userName || '未登录',
    userAvatar: state =>
        state.userInfo?.avatar
            ? state.HOST + state.userInfo.avatar
            : require('@/assets/image/image2.png')
  },
  mutations: {
    SET_USERINFO(state, payload) {
      state.userInfo = payload
      window.localStorage.setItem('user_info', JSON.stringify(payload))
    },
    CLEAR_USERINFO(state) {
      state.userInfo = {}
      window.localStorage.removeItem('user_info')
    }
  },
  actions: {
    logout({ commit }) {
      return new Promise(resolve => {
        logout().then(() => {
          commit('CLEAR_USERINFO')
          window.localStorage.removeItem('user_token')
          resolve()
        })
      })
    },
    updateUserInfo({ commit }) {
      return getUser().then(res => {
        if (res.code === 1000) {
          commit('SET_USERINFO', res.data)
        }
      })
    }
  },
  modules: {}
})