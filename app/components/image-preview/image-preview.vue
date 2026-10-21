<template>
	<view class="container">
		<image :src="realSrc" :style="{width: realWidth, height: realHeight}" mode="aspectFill" @click="open" />
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		computed
	} from 'vue'
	import {
		isExternal
	} from "@/utils/validate";
	import config from '@/config';

	const {
		proxy
	} = getCurrentInstance()

	const props = defineProps({
		src: {
			type: String,
			default: ""
		},
		width: {
			type: [Number, String],
			default: ""
		},
		height: {
			type: [Number, String],
			default: ""
		}
	});
	const styleScale = computed(() => {
		return 'scale(' + scale.value + ')';
	});

	const realSrc = computed(() => {
		if (!props.src) {
			return;
		}
		let real_src = props.src.split(",")[0];
		if (isExternal(real_src)) {
			return real_src;
		}
		return config.baseUrl + real_src;
	});

	const realSrcList = computed(() => {
		if (!props.src) {
			return;
		}
		let real_src_list = props.src.split(",");
		let srcList = [];
		real_src_list.forEach(item => {
			if (isExternal(item)) {
				return srcList.push(item);
			}
			return srcList.push(config.baseUrl + item);
		});
		return srcList;
	});

	const realWidth = computed(() =>
		typeof props.width == "string" ? props.width : `${props.width}px`
	);

	const realHeight = computed(() =>
		typeof props.height == "string" ? props.height : `${props.height}px`
	);
	
	const open = () => {
		// 预览图片
		uni.previewImage({
			urls: realSrcList.value
		});
	}
	
</script>

<style lang="scss" scoped>
	.container {
		width: 100%;
		height: 100%;
		display: inline-block;
	}
</style>