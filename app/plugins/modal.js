import useModalStore from '@/store/modules/modal';

export default {
	// 消息提示
	msg(content) {
		uni.showToast({
			title: content,
			icon: 'none'
		})
	},
	// 错误消息
	msgError(content) {
		uni.showToast({
			title: content,
			icon: 'error'
		})
	},
	// 成功消息
	msgSuccess(content) {
		uni.showToast({
			title: content,
			icon: 'success'
		})
	},
	// 隐藏消息
	hideMsg(content) {
		uni.hideToast()
	},
	// 弹出提示
	alert(content, title) {
		const modalStore = useModalStore();
		uni.navigateTo({
			url: '/pages/common/popup/index',
			success: function(res) {
				modalStore.open({
					title: title || '系统提示',
					content: content,
					cancelText: '取消',
					confirmText: '确定',
					confirm: function(res) {}
				})
			}
		})
	},
	// 确认窗体
	confirm(content, title) {
		const modalStore = useModalStore();
		return new Promise((resolve, reject) => {
			uni.navigateTo({
				url: '/pages/common/popup/index',
				success: function(res) {
					modalStore.open({
						title: title || '系统提示',
						content: content,
						cancelText: '取消',
						confirmText: '确定',
						confirm: function(res) {
							if (res.confirm) {
								resolve(true)
							} else if (res.cancel) {
								reject(false)
							}
						}
					})
				}
			})

		})
	},
	// 提示信息
	showToast(option) {
		if (typeof option === "object") {
			uni.showToast(option)
		} else {
			uni.showToast({
				title: option,
				icon: "none",
				duration: 2500
			})
		}
	},
	// 打开遮罩层
	loading(content) {
		uni.showLoading({
			title: content,
			icon: 'none'
		})
	},
	// 关闭遮罩层
	closeLoading() {
		uni.hideLoading()
	}
}