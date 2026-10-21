<template>
	<view class="container">

		<view class="form-container">
			<uni-forms ref="form" :model="user" labelWidth="0">
				<view class="form-card">
					<view class="form-card-title">
						<text class="title-text">基本信息</text>
					</view>

					<view class="form-item">
						<text class="label">昵称</text>
						<uni-easyinput class="input" v-model="user.nickName" placeholder="请输入昵称" />
					</view>

					<view class="form-item">
						<text class="label">手机号</text>
						<uni-easyinput class="input" v-model="user.phonenumber" placeholder="请输入手机号码" />
					</view>

					<view class="form-item">
						<text class="label">性别</text>
						<view class="radio-group">
							<uni-data-checkbox v-model="user.sex" :localdata="sexs" />
						</view>
					</view>
				</view>

				<view class="form-card">
					<view class="form-card-title">
						<text class="title-text">其他信息</text>
					</view>

					<view class="form-item">
						<text class="label">邮箱</text>
						<uni-easyinput class="input" v-model="user.email" placeholder="请输入邮箱地址" />
					</view>
				</view>
			</uni-forms>

			<view class="button-group">
				<button class="submit-btn" @click="submit">保存信息</button>
				<button class="cancel-btn" @click="goBack">取消</button>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		getUserProfile
	} from "@/api/system/user"
	import {
		updateUserProfile
	} from "@/pages/api/system/user"
	import {
		onReady
	} from '@dcloudio/uni-app'
	import {
		ref,
		getCurrentInstance,
	} from 'vue'

	const {
		proxy
	} = getCurrentInstance();

	const user = ref({
		nickName: "",
		phonenumber: "",
		email: "",
		sex: ""
	})

	const sexs = ref([{
		text: '男',
		value: "0"
	}, {
		text: '女',
		value: "1"
	}])

	const rules = ref({
		nickName: {
			rules: [{
				required: true,
				errorMessage: '用户昵称不能为空'
			}]
		},
		phonenumber: {
			rules: [{
				required: true,
				errorMessage: '手机号码不能为空'
			}, {
				pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
				errorMessage: '请输入正确的手机号码'
			}]
		},
		email: {
			rules: [{
				required: true,
				errorMessage: '邮箱地址不能为空'
			}, {
				format: 'email',
				errorMessage: '请输入正确的邮箱地址'
			}]
		}
	})

	onReady(() => {
		proxy.$refs.form.setRules(rules.value)
	})

	const getUser = () => {
		getUserProfile().then(response => {
			user.value = response.data
		})
	}

	const submit = () => {
		proxy.$refs.form.validate().then(res => {
			updateUserProfile(user.value).then(response => {
				proxy.$modal.msgSuccess("修改成功")
				setTimeout(() => {
					goBack()
				}, 1000)
			})
		})
	}

	const goBack = () => {
		uni.navigateBack()
	}

	getUser()
</script>

<style lang="scss" scoped>
	page {
		background-color: #f5f5f5;
	}

	.container {
		padding-top: 20px;
		min-height: 100vh;
		padding-bottom: 20px;
	}

	.profile-header {
		background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
		padding: 30px 20px;
		border-bottom-left-radius: 20px;
		border-bottom-right-radius: 20px;
		box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

		.avatar-container {
			display: flex;
			flex-direction: column;
			align-items: center;

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

				.icon {
					font-size: 40px;
					color: white;
				}
			}

			.username {
				color: white;
				font-size: 20px;
				font-weight: 500;
			}
		}
	}

	.form-container {
		padding: 20px;
		position: relative;
		top: -30px;
	}

	.form-card {
		background: white;
		border-radius: 15px;
		padding: 20px;
		margin-bottom: 20px;
		box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

		.form-card-title {
			border-left: 4px solid #4facfe;
			padding-left: 10px;
			margin-bottom: 20px;

			.title-text {
				font-size: 18px;
				font-weight: 600;
				color: #333;
			}
		}

		.form-item {
			margin-bottom: 20px;

			&:last-child {
				margin-bottom: 0;
			}

			.label {
				display: block;
				font-size: 15px;
				color: #333;
				margin-bottom: 10px;
				font-weight: 500;
			}

			.input {
				::v-deep .uni-easyinput__content {
					border-radius: 10px;
					border: 1px solid #e5e5e5;
				}
			}

			.radio-group {
				::v-deep .uni-data-checklist {
					flex-direction: row !important;
				}

				::v-deep .checklist-group {
					flex-direction: row !important;
				}

				::v-deep .uni-label-pointer {
					margin-right: 20px;
				}
			}
		}
	}

	.button-group {
		display: flex;
		flex-direction: column;
		gap: 15px;
		padding: 0 10px;

		.submit-btn {
			background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
			border: none;
			color: white;
			font-size: 16px;
			font-weight: 500;
			border-radius: 10px;
			height: 45px;
			line-height: 45px;
		}

		.cancel-btn {
			background: #f8f9fa;
			border: 1px solid #e5e5e5;
			color: #666;
			font-size: 16px;
			font-weight: 500;
			border-radius: 10px;
			height: 45px;
			line-height: 45px;
		}
	}
</style>
