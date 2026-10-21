<template>
	<view class="container">

		<view class="content">
			<view class="info-section">
				<view class="section-title">基本信息</view>
				<view class="info-card">
					<view class="info-item">
						<text class="info-label">用户昵称</text>
						<text class="info-value">{{ user.nickName }}</text>
					</view>
					<view class="info-item">
						<text class="info-label">手机号码</text>
						<text class="info-value">{{ user.phonenumber }}</text>
					</view>
					<view class="info-item">
						<text class="info-label">用户邮箱</text>
						<text class="info-value">{{ user.email }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		getUserProfile
	} from "@/api/system/user"
	import {
		ref,
		getCurrentInstance,
	} from 'vue'
	const {
		proxy
	} = getCurrentInstance();

	const user = ref({});
	const roleGroup = ref("");
	const postGroup = ref("");
	const getUser = () => {
		getUserProfile().then(response => {
			user.value = response.data
			roleGroup.value = response.roleGroup
			postGroup.value = response.postGroup
		})
	}
	getUser()
</script>

<style lang="scss" scoped>
page {
	background-color: #f5f5f5;
}

.container {
	padding-top: 20px;
}

.user-header {
	background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
	padding: 30px 20px;
	border-bottom-left-radius: 20px;
	border-bottom-right-radius: 20px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

	.header-content {
		display: flex;
		flex-direction: column;
		align-items: center;

		.avatar-container {
			.avatar-placeholder {
				width: 80px;
				height: 80px;
				border-radius: 50%;
				background: rgba(255, 255, 255, 0.2);
				display: flex;
				align-items: center;
				justify-content: center;
				margin-bottom: 15px;
				border: 3px solid rgba(255, 255, 255, 0.3);

				.avatar-icon {
					font-size: 40px;
					color: white;
				}
			}
		}

		.username {
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

.info-section {
	margin-bottom: 20px;

	.section-title {
		font-size: 18px;
		font-weight: 600;
		color: #333;
		margin-bottom: 15px;
		margin-left: 5px;
	}

	.info-card {
		background: white;
		border-radius: 15px;
		padding: 20px;
		box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

		.info-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 12px 0;
			border-bottom: 1px solid #f0f0f0;

			&:last-child {
				border-bottom: none;
			}

			.info-label {
				font-size: 15px;
				color: #666;
			}

			.info-value {
				font-size: 15px;
				color: #333;
				font-weight: 500;
				max-width: 60%;
				text-align: right;
				word-break: break-all;
			}
		}
	}
}
</style>
