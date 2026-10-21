<template>
	<view class="container">
		<view class="setting-header">
			<view class="header-content">
				<view class="icon-container">
					<view class="iconfont icon-setting setting-icon"></view>
				</view>
				<text class="header-title">应用设置</text>
			</view>
		</view>
		
		<view class="content">
			<view class="menu-list">
				<view class="menu-item" @click="handleToPwd">
					<view class="item-content">
						<view class="iconfont icon-password menu-icon"></view>
						<text class="menu-text">修改密码</text>
					</view>
					<view class="iconfont icon-right arrow-icon"></view>
				</view>
				
				<view class="menu-item" @click="handleCleanTmp">
					<view class="item-content">
						<view class="iconfont icon-clean menu-icon"></view>
						<text class="menu-text">清理缓存</text>
					</view>
					<view class="iconfont icon-right arrow-icon"></view>
				</view>
			</view>
			
			<view class="logout-section">
				<button class="logout-btn" @click="handleLogout">退出登录</button>
			</view>
		</view>
	</view>
</template>

<script setup>
import {getCurrentInstance, ref} from 'vue'

import useUserStore from '@/store/modules/user'

const userStore = useUserStore();
const {proxy} = getCurrentInstance();
const windowHeight = ref(0);
const getWindowHeight = () => {
    windowHeight.value = uni.getSystemInfoSync().windowHeight
}

const handleToPwd = () => {
    proxy.$tab.navigateTo('/pages/mine/pwd/index')
}

const handleCleanTmp = () => {
    proxy.$modal.confirm('确定清空缓存吗？登陆状态会失效。').then(res => {
        userStore.logOut().then(res => {
            uni.clearStorageSync()
            proxy.$tab.reLaunch('/pages/login')
        })
    })
}

const handleLogout = () => {
    proxy.$modal.confirm('确定注销并退出系统吗？').then(res => {
        userStore.logOut().then(res => {
            proxy.$tab.reLaunch('/pages/login')
        })
    })
}

getWindowHeight()
</script>

<style lang="scss" scoped>
page {
	background-color: #f5f5f5;
}

.container {
	min-height: 100vh;
	padding-bottom: 20px;
}

.setting-header {
	background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
	padding: 30px 20px;
	border-bottom-left-radius: 20px;
	border-bottom-right-radius: 20px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
	
	.header-content {
		display: flex;
		flex-direction: column;
		align-items: center;
		
		.icon-container {
			width: 70px;
			height: 70px;
			border-radius: 50%;
			background: rgba(255, 255, 255, 0.2);
			display: flex;
			align-items: center;
			justify-content: center;
			margin-bottom: 15px;
			border: 3px solid rgba(255, 255, 255, 0.3);
			
			.setting-icon {
				font-size: 36px;
				color: white;
			}
		}
		
		.header-title {
			color: white;
			font-size: 22px;
			font-weight: 500;
		}
	}
}

.content {
	padding: 20px;
	position: relative;
	top: -30px;
}

.menu-list {
	background: white;
	border-radius: 15px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
	overflow: hidden;
	margin-bottom: 20px;
}

.menu-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 20px;
	border-bottom: 1px solid #f0f0f0;
	
	&:last-child {
		border-bottom: none;
	}
	
	.item-content {
		display: flex;
		align-items: center;
		
		.menu-icon {
			font-size: 20px;
			color: #4facfe;
			margin-right: 15px;
			width: 24px;
			text-align: center;
		}
		
		.menu-text {
			font-size: 16px;
			color: #333;
		}
	}
	
	.arrow-icon {
		font-size: 16px;
		color: #ccc;
	}
}

.logout-section {
	padding: 0 10px;
}

.logout-btn {
	background: #fff;
	border: 1px solid #ff4d4f;
	color: #ff4d4f;
	font-size: 16px;
	font-weight: 500;
	border-radius: 10px;
	height: 45px;
	line-height: 45px;
	width: 100%;
}
</style>