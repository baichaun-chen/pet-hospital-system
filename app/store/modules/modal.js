import {
	defineStore
} from 'pinia'

const useModalStore = defineStore('modal', {
	state: () => ({
		type: '',
		cancelText: '',
		confirmText: '',
		title: '',
		content: '',
		isShow: false,
		confirm: function() {},
	}),
	actions: {
		open(obj) {
			this.type = obj.type || 'center'
			this.cancelText = obj.cancelText || '取消'
			this.confirmText = obj.confirmText || '确定'
			this.title = obj.title || '提示'
			this.content = obj.content || ''
			this.confirm = obj.confirm || function(){}
			this.isShow = false
			this.isShow = true
		},
		close() {
			this.type = 'center'
			this.cancelText = '取消'
			this.confirmText = '确定'
			this.title = '提示'
			this.content = ''
			this.confirm = function(){}
			this.isShow = false
		}
	}
})

export default useModalStore