<template>
	<view class="mine-container" :style="{height: `${windowHeight}px`}">
		<!--顶部个人信息栏-->
		<view class="header-section">
			<view class="user-profile">
				<view class="avatar-container">
					<view v-if="!userStore.avatar" class="cu-avatar xl round bg-white">
						<view class="iconfont icon-people text-gray icon"></view>
					</view>
					<image v-if="userStore.avatar" @click="handleToAvatar" :src="userStore.avatar" class="cu-avatar xl round"
						mode="widthFix">
					</image>
				</view>
				<view class="user-info-container">
					<view v-if="!userStore.name" @click="handleToLogin" class="login-tip">
						点击登录
					</view>
					<view v-if="userStore.name" @click="handleToInfo" class="user-name">
						{{ userStore.name }}
					</view>
				</view>
				<view @click="handleToInfo" class="edit-profile-btn">
					<text>个人信息</text>
					<view class="iconfont icon-right"></view>
				</view>
			</view>
		</view>

		<view class="content-section">
			<!-- 功能菜单 -->
			<view class="menu-grid">
				<view class="menu-item" @click="handleToEditInfo">
					<view class="icon-container bg-blue">
						<view class="iconfont icon-user icon"></view>
					</view>
					<text class="menu-text">编辑资料</text>
				</view>
				<view class="menu-item" @click="handleToSetting">
					<view class="icon-container bg-green">
						<view class="iconfont icon-setting icon"></view>
					</view>
					<text class="menu-text">应用设置</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import storage from '@/utils/storage'
	import {
		ref,
		getCurrentInstance,
	} from 'vue'
	
	import useUserStore from '@/store/modules/user'
	
	const userStore = useUserStore();
	
	const {
		proxy
	} = getCurrentInstance();
	
	const version = ref(getApp().globalData.config.appInfo.version);
	const windowHeight = ref(0);
	
	const getWindowHeight = () => {
		windowHeight.value = uni.getSystemInfoSync().windowHeight - 50
	}

	const handleToInfo = () => {
		proxy.$tab.navigateTo('/pages/mine/info/index')
	}
	const handleToEditInfo = () => {
		proxy.$tab.navigateTo('/pages/mine/info/edit')
	}
	const handleToSetting = () => {
		proxy.$tab.navigateTo('/pages/mine/setting/index')
	}
	const handleToLogin = () => {
		proxy.$tab.reLaunch('/pages/login')
	}
	const handleToAvatar = () => {
		uni.$once('update', function (){
			userStore.getInfo()
		})
		proxy.$tab.navigateTo('/pages/mine/avatar/index')
	}
	
	getWindowHeight()
	userStore.getInfo()
</script>

<style lang="scss">
	page {
		background-color: #f8f9fa;
	}

	.mine-container {
		width: 100%;
		height: 100%;
		
		.header-section {
			padding: 30px 20px;
			background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
			color: white;
			border-bottom-left-radius: 20px;
			border-bottom-right-radius: 20px;
			box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
			
			.user-profile {
				display: flex;
				align-items: center;
				justify-content: space-between;
				
				.avatar-container {
					.cu-avatar {
						width: 70px;
						height: 70px;
						border: 3px solid rgba(255, 255, 255, 0.3);
						box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
						
						.icon {
							font-size: 40px;
						}
					}
				}
				
				.user-info-container {
					flex: 1;
					margin-left: 15px;
					
					.login-tip {
						font-size: 20px;
						font-weight: 500;
						margin-bottom: 5px;
					}
					
					.user-name {
						font-size: 22px;
						font-weight: 600;
					}
				}
				
				.edit-profile-btn {
					display: flex;
					align-items: center;
					background: rgba(255, 255, 255, 0.2);
					padding: 8px 15px;
					border-radius: 20px;
					font-size: 14px;
					backdrop-filter: blur(10px);
					
					.icon-right {
						margin-left: 5px;
					}
				}
			}
		}

		.content-section {
			position: relative;
			top: -30px;
			padding: 0 15px;
			
			.menu-grid {
				display: flex;
				flex-wrap: wrap;
				background: white;
				border-radius: 15px;
				padding: 20px 0;
				box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
				
				.menu-item {
					flex: 0 0 50%;
					text-align: center;
					margin: 5px 0;
					display: flex;
					flex-direction: column;
					align-items: center;
					
					.icon-container {
						width: 45px;
						height: 45px;
						border-radius: 50%;
						display: flex;
						align-items: center;
						justify-content: center;
						margin-bottom: 8px;
						color: white;
						box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
						
						.icon {
							font-size: 20px;
						}
					}
					
					.menu-text {
						font-size: 15px;
						color: #333;
						font-weight: 500;
					}
				}
			}
		}
	}
</style>