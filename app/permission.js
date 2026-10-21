import {
	getToken
} from '@/utils/auth'
import useUserStore from '@/store/modules/user'

// 登录页面
const loginPage = "/pages/login"

// 页面白名单
const whiteList = [
	'/pages/login', 
	'/pages/register', 
	'/pages/common/popup/index',
	'/pages/common/webview/index',
	'/pages/common/textview/index'
]

// 检查地址白名单
function checkWhite(url) {
	const path = url.split('?')[0]
	for (let s of whiteList) {
		let ss = s.split('/**')
		if(url.indexOf(ss[0]) !== -1){
			return true;
		}
	}
	return whiteList.indexOf(path) !== -1
}

// 判断登陆状态
async function loginStatus() {
	return await useUserStore().getInfo()
}

// 页面跳转验证拦截器
let list = ["navigateTo", "redirectTo", "reLaunch", "switchTab"]
list.forEach(item => {
	uni.addInterceptor(item, {
		invoke(to) {
			if (getToken() !== '') {
				// if (to.url === '/pages/index' || typeof useUserStore().id !== 'number') loginStatus()
				return true
			} else {
				if (checkWhite(to.url)) {
					return true
				}
				uni.navigateTo({
					url: loginPage
				})
				return false
			}
		},
		fail(err) {
			console.log(err)
		}
	})
})