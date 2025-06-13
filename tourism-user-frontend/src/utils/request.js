import axios from 'axios'
import store from '@/store'
import { Message, MessageBox } from 'element-ui'

// 创建axios实例
const instance = axios.create({
    baseURL: "http://localhost:8080", // 根据你的后端实际地址调整
    timeout: 5000
})

// 请求拦截器
instance.interceptors.request.use(
    function (config) {
        // POST请求添加时间戳
        if (config.method === 'post') {
            config.data = {
                ...config.data,
                _t: Date.now() / 1000
            }
        } else if (config.method === 'get') {
            config.params = {
                random: Math.random(),
                ...config.params
            }
        }
        // 请求头自动带上token
        const token = window.localStorage.getItem("user_token")
        if (token) {
            config.headers.x_access_token = token
        }
        return config
    },
    function (err) {
        return Promise.reject(err)
    }
)

// 响应拦截器
instance.interceptors.response.use(
    function (response) {
        // 只用业务code判断登录状态
        const res = response
        if (res.data.code === 1011) {
            MessageBox.alert('系统登陆已过期，请重新登录', '错误', {
                confirmButtonText: '确定',
                type: 'error'
            }).then(() => {
                store.dispatch('logout').then(() => {
                    window.localStorage.removeItem("user_token")
                    window.localStorage.removeItem("user_info")
                    location.reload()
                })
            })
            return Promise.reject('error')
        } else if (res.data.code === 1009) {
            MessageBox.alert('该账号已被锁定', '错误', {
                confirmButtonText: '确定',
                type: 'error'
            }).then(() => {
                store.dispatch('logout').then(() => {
                    window.localStorage.removeItem("user_token")
                    window.localStorage.removeItem("user_info")
                    location.reload()
                })
            })
            return Promise.reject('error')
        } else {
            return res.data
        }
    },
    function (err) {
        // 只做错误提示，不清token
        MessageBox.alert('网络异常或服务器无响应，请稍后再试', '错误', {
            confirmButtonText: '确定',
            type: 'error'
        })
        return Promise.reject(err)
    }
)

// 封装 get/post 方法
export function get(url, params) {
    return instance.get(url, { params })
}

export function post(url, data) {
    return instance.post(url, data)
}

export default instance