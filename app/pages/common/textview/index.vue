<template>
	<view>
		<uni-card class="view-title" :title="title">
			<text class="uni-body view-content">{{ content }}</text>
		</uni-card>
	</view>
</template>

<script setup>
	import {
		onLoad
	} from "@dcloudio/uni-app"
	import {
		ref,
	} from 'vue'

	const title = ref('');
	const content = ref('');
	onLoad((options) => {
		title.value = options.title
		content.value = options.content
		uni.setNavigationBarTitle({
			title: options.title
		})
	})
</script>

<style scoped>
	page {
		background-color: #ffffff;
	}

	.view-title {
		font-weight: bold;
	}

	.view-content {
		font-size: 26rpx;
		padding: 12px 5px 0;
		color: #333;
		line-height: 24px;
		font-weight: normal;
	}
</style>