<template>
	<view>
		<uni-popup ref="alertDialogRef" type="dialog">
			<uni-popup-dialog :type="modalStore.type" :cancelText="modalStore.cancelText" :confirmText="modalStore.confirmText"
				:title="modalStore.title" :content="modalStore.content" @confirm="confirm" @close="close">
			</uni-popup-dialog>
		</uni-popup>
	</view>
</template>
<script setup>
	import {
		ref,
		watch,
		onMounted,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onUnload
	} from '@dcloudio/uni-app'
	
	import useModalStore from '@/store/modules/modal';
	
	const proxy = getCurrentInstance()
	const modalStore = useModalStore();
	const alertDialogRef = ref(null); 
	
	onLoad(params => {
		
	})

	onMounted(() => {
		if(modalStore.isShow) alertDialogRef.value.open()
	})
	
	watch(() => modalStore.isShow, (value) => {
		if (value) alertDialogRef.value.open()
		else alertDialogRef.value.close()
	})
	const confirm = (val) => {
		const res = {confirm: true, cancel: false, value: val}
		modalStore.isShow = false
		uni.navigateBack({
			success:function(){
				modalStore.confirm(res)
			}
		})
	}
	const close = (val) => {
		const res = {confirm: false, cancel: true, value: val}
		modalStore.isShow = false
		uni.navigateBack({
			success:function(){
				modalStore.confirm(res)
			}
		})
	}
	
	onUnload(() => {
		modalStore.isShow = false
	})
</script>
<style lang="scss">
	
	page{
		background-color: #ffffffff;
	}
	
	@mixin flex {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
	}

	@mixin height {
		/* #ifndef APP-NVUE */
		height: 100%;
		/* #endif */
		/* #ifdef APP-NVUE */
		flex: 1;
		/* #endif */
	}

	.box {
		@include flex;
	}

	.button {
		@include flex;
		align-items: center;
		justify-content: center;
		flex: 1;
		height: 35px;
		margin: 0 5px;
		border-radius: 5px;
	}

	.example-body {
		background-color: #fff;
		padding: 10px 0;
	}

	.button-text {
		color: #fff;
		font-size: 12px;
	}

	.popup-content {
		@include flex;
		align-items: center;
		justify-content: center;
		padding: 15px;
		height: 50px;
		background-color: #fff;
	}

	.popup-height {
		@include height;
		width: 200px;
	}

	.text {
		font-size: 12px;
		color: #333;
	}

	.popup-success {
		color: #fff;
		background-color: #e1f3d8;
	}

	.popup-warn {
		color: #fff;
		background-color: #faecd8;
	}

	.popup-error {
		color: #fff;
		background-color: #fde2e2;
	}

	.popup-info {
		color: #fff;
		background-color: #f2f6fc;
	}

	.success-text {
		color: #09bb07;
	}

	.warn-text {
		color: #e6a23c;
	}

	.error-text {
		color: #f56c6c;
	}

	.info-text {
		color: #909399;
	}

	.dialog,
	.share {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
	}

	.dialog-box {
		padding: 10px;
	}

	.dialog .button,
	.share .button {
		/* #ifndef APP-NVUE */
		width: 100%;
		/* #endif */
		margin: 0;
		margin-top: 10px;
		padding: 3px 0;
		flex: 1;
	}

	.dialog-text {
		font-size: 14px;
		color: #333;
	}
</style>

