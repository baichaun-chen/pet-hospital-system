import config from '@/config'
import {
	getToken
} from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import {
	toast,
	showConfirm,
	tansParams
} from '@/utils/common'
import {
	FormData
} from '@/utils/formData/formData'
import useModalStore from '@/store/modules/modal';
import useUserStore from '@/store/modules/user';

let timeout = 20000
const baseUrl = config.baseUrl
const isCloud = config.isCloud
const apiReplace = config.apiReplace

const upload = config => {
	// 是否需要设置 token
	const isToken = (config.headers || {}).isToken === false
	config.header = config.header || {}
	if (getToken() && !isToken) {
		config.header['Authorization'] = 'Bearer ' + getToken()
	}

	if (isCloud) {
		//判断请求路径是否是Vue和Cloud的差异路径
		for (let obj of apiReplace) {
			//检查通配项
			if (obj.vueUrl === config.url) {
				config.url = obj.cloudUrl
				config.method = obj.method || config.method
				break;
			} else {
				let uris = obj.vueUrl.split('/');
				//replaceAll在部分手机APP上可能不兼容
				if (uris[uris.length - 1] == '**' && config.url.includes(obj.vueUrl.replace('/**', ''))) {
					config.url = config.url.replace(obj.vueUrl.split('/**')[0], obj.cloudUrl.split('/**')[0])
					config.method = obj.method || config.method
					break;
				}
			}
		}
	}

	// get请求映射params参数
	if (config.params) {
		let url = config.url + '?' + tansParams(config.params)
		url = url.slice(0, -1)
		config.url = url
	}
	return new Promise((resolve, reject) => {
		//#ifndef APP
		//new一个FormData对象
		let formData = new FormData()
		//调用它的append()方法来添加字段或者调用appendFile()方法添加文件
		formData.appendFile(config.name, config.file.path, config.file.name)
		let data = formData.getData()
		config.header["content-type"] = data.contentType
		uni.request({
			timeout: config.timeout || timeout,
			url: baseUrl + config.url,
			header: config.header,
			method: 'post',
			data: data.buffer,
			dataType: 'json',
		}).then(response => {
			let res = response
			if (res.statusCode != 200) {
				toast('后端接口连接异常')
				reject('后端接口连接异常')
				return
			}
			const code = res.data.code || 200
			const msg = errorCode[code] || res.data.msg || errorCode['default']
			if (code == 401) {
				uni.navigateTo({
					url: '/pages/common/popup/index',
					success: function(res) {
						useModalStore().open({
							title: '系统提示',
							content: '无效的会话，或者会话已过期，请重新登录。',
							cancelText: '取消',
							confirmText: '确定',
							confirm: function(res) {
								if (res.confirm) {
									uni.navigateTo({
										url: '/pages/login'
									})
								} else {
									useUserStore().logOut()
								}
							}
						})
					}
				})
				reject('无效的会话，或者会话已过期，请重新登录。')
			} else if (code === 500) {
				toast(msg)
				reject('500')
			} else if (code !== 200) {
				toast(msg)
				reject(code)
			}
			resolve(res.data)
		}).catch(error => {
			let {
				message,
				errMsg
			} = error
			if (message === undefined) {
				message = '未知异常'
				if (errMsg.includes('ERR_CERT_COMMON_NAME_INVALID')) message = 'ssl证书已到期或证书与域名不匹配'
			} else if (message === 'Network Error') {
				message = '后端接口连接异常'
			} else if (message.includes('timeout')) {
				message = '系统接口请求超时'
			} else if (message.includes('Request failed with status code')) {
				message = '系统接口' + message.substr(message.length - 3) + '异常'
			}
			toast(message)
			reject(error)
		})
		//#endif
		//#ifdef APP || H5
		uni.uploadFile({
			timeout: config.timeout || timeout,
			url: baseUrl + config.url,
			filePath: config.file.path,
			name: config.name || 'file',
			header: config.header,
			formData: config.formData,
			dataType: 'json',
			success: (res) => {
				res.data = JSON.parse(res.data)
				if (res.statusCode != 200) {
					toast('后端接口连接异常')
					reject('后端接口连接异常')
					return
				}
				const code = res.data.code || 200
				const msg = errorCode[code] || res.data.msg || errorCode['default']
				if (code == 401) {
					uni.navigateTo({
						url: '/pages/common/popup/index',
						success: function(res) {
							useModalStore().open({
								title: '系统提示',
								content: '无效的会话，或者会话已过期，请重新登录。',
								cancelText: '取消',
								confirmText: '确定',
								confirm: function(res) {
									if (res.confirm) {
										uni.navigateTo({
											url: '/pages/login'
										})
									} else {
										useUserStore().logOut()
									}
								}
							})
						}
					})
					reject('无效的会话，或者会话已过期，请重新登录。')
				} else if (code === 500) {
					toast(msg)
					reject('500')
				} else if (code !== 200) {
					toast(msg)
					reject(code)
				}
				resolve(res.data)
			},
			fail: (error) => {
				let {
					message,
					errMsg
				} = error
				if (message === undefined) {
					message = '未知异常'
					if (errMsg.includes('ERR_CERT_COMMON_NAME_INVALID')) message =
						'ssl证书已到期或证书与域名不匹配'
				} else if (message === 'Network Error') {
					message = '后端接口连接异常'
				} else if (message.includes('timeout')) {
					message = '系统接口请求超时'
				} else if (message.includes('Request failed with status code')) {
					message = '系统接口' + message.substr(message.length - 3) + '异常'
				}
				toast(message)
				reject(error)
			}
		})
		//#endif
	})
}

export default upload
