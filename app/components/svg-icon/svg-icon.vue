<template>
	<view class="svg">
		<image :src="useIcons(icon)" :lazy-load="lazyLoad" :fade-show="fadeShow" style="width: 16px;height: 20px;" mode="widthFix" />
	</view>
</template>

<script>
	import config from "@/config";

	// const baseUrl = config.baseUrl
	/**
	 * 引用导入的SVG图片
	 * @description 可以通过svg图标的名字直接引用/static/icons/svg/路径下的svg图标
	 * @property {String} icon 图片资源地址
	 * @property {String} lazyLoad 图片裁剪、缩放的模式具体参考uni-app的image文档（仅微信小程序、百度小程序、抖音小程序、飞书小程序）
	 * @property {String} fadeShow 图片显示动画效果（仅App-nvue 2.3.4+ Android有效）
	 */
	export default {
		props: {
			icon: String,
			lazyLoad: Boolean, //图片裁剪、缩放的模式
			fadeShow: Boolean,
		},
		methods: {
			useIcons(name) {
				var icon = this.icon;
				if (icon == null || icon == undefined || icon == '' || icon == '#') return '';
				return '/static/icons/svg/' + name + '.svg'
			},
		}
	}
</script>

<style lang="scss" scoped>
	.svg {
		display: inline-block;
	}
</style>
