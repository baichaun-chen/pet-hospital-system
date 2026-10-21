<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="appStore.sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <breadcrumb v-if="!settingsStore.topNav" id="breadcrumb-container" class="breadcrumb-container" />
    <top-nav v-if="settingsStore.topNav" id="topmenu-container" class="topmenu-container" />

    <div class="right-menu">
      <template v-if="appStore.device !== 'mobile'">
        <div class="welcome-container right-menu-item">
          <span>您好, {{ userStore.name }}</span>
        </div>
        <el-button type="text" @click.native="logout" class="right-menu-item logout-btn">
          <svg-icon icon-class="logout" style="font-size: 15px" class-name='custom-class' />
          <el-link type="primary">注销</el-link>
        </el-button>
        <header-search id="header-search" class="right-menu-item hover-effect" />

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <el-tooltip content="主题模式" effect="dark" placement="bottom">
          <div class="right-menu-item hover-effect theme-switch-wrapper" @click="toggleTheme">
            <svg-icon v-if="settingsStore.isDark" icon-class="sunny" />
            <svg-icon v-if="!settingsStore.isDark" icon-class="moon" />
          </div>
        </el-tooltip>

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>
      </template>
      <div class="avatar-container">
        <el-dropdown @command="handleCommand" class="right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <img :src="userStore.avatar" class="user-avatar" />
            <el-icon><caret-bottom /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <router-link to="/user/profile">
                <el-dropdown-item>个人中心</el-dropdown-item>
              </router-link>
              <el-dropdown-item command="setLayout" v-if="settingsStore.showSettings">
                <span>布局设置</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessageBox } from 'element-plus'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import HeaderSearch from '@/components/HeaderSearch'
import useAppStore from '@/store/modules/app'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import {getUser} from "@/api/system/user.js";

const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()


const getList = () => {
  getUser()
}

function toggleSideBar() {
  appStore.toggleSideBar()
}

function handleCommand(command) {
  switch (command) {
    case "setLayout":
      setLayout()
      break
    case "logout":
      logout()
      break
    default:
      break
  }
}

function logout() {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logOut().then(() => {
      location.href = '/index'
    })
  }).catch(() => { })
}

const emits = defineEmits(['setLayout'])
function setLayout() {
  emits('setLayout')
}

function toggleTheme() {
  settingsStore.toggleTheme()
}

getList()
</script>

<style lang='scss' scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: var(--navbar-bg);
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  padding: 0 20px;

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;
    margin-right: 20px;
    border-radius: 6px;
    padding: 0 8px;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    flex: 1;
    height: 100%;
    display: flex;
    align-items: center;
  }

  .topmenu-container {
    flex: 1;
    height: 100%;
    position: static;
    margin-left: 20px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    height: 100%;
    display: flex;
    align-items: center;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: flex;
      align-items: center;
      padding: 6px 10px;
      height: 36px;
      font-size: 16px;
      color: var(--navbar-text);
      vertical-align: text-bottom;
      border-radius: 6px;
      margin: 0 2px;
      transition: all 0.3s ease;
      position: relative;

      &.hover-effect {
        cursor: pointer;

        &:hover {
          background: rgba(0, 0, 0, 0.05);
          transform: translateY(-1px);
        }
      }

      &.theme-switch-wrapper {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 36px;
        height: 36px;
        padding: 0;
        border-radius: 50%;

        svg {
          transition: all 0.3s ease;
          font-size: 18px;

          &:hover {
            transform: scale(1.15) rotate(10deg);
          }
        }
      }
    }

    .welcome-container {
      font-size: 14px;
      padding: 0 12px;
      background: linear-gradient(90deg, var(--el-color-primary-light-9), var(--el-color-primary-light-8));
      border-radius: 18px;
      margin-right: 8px;
      font-weight: 500;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    }

    .logout-btn {
      padding: 6px 12px;
      border-radius: 18px;
      margin: 0 8px;
      background: linear-gradient(90deg, var(--el-color-primary-light-9), var(--el-color-primary-light-8));
      border: 1px solid var(--el-color-primary-light-5);
      transition: all 0.3s ease;

      &:hover {
        background: linear-gradient(90deg, var(--el-color-primary-light-8), var(--el-color-primary-light-7));
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        transform: translateY(-1px);
      }

      :deep(.el-link) {
        font-size: 14px;
        font-weight: 500;
      }
    }

    .avatar-container {
      margin-left: 15px;
      margin-right: 10px;

      .avatar-wrapper {
        display: flex;
        align-items: center;
        position: relative;
        cursor: pointer;
        padding: 2px;

        .user-avatar {
          cursor: pointer;
          width: 36px;
          height: 36px;
          border-radius: 50%;
          object-fit: cover;
          border: 2px solid #fff;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
          transition: all 0.3s ease;

          &:hover {
            transform: scale(1.05);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
          }
        }

        i {
          cursor: pointer;
          position: absolute;
          right: -10px;
          top: 50%;
          transform: translateY(-50%);
          font-size: 12px;
          color: #999;
          transition: all 0.3s ease;
        }

        &:hover {
          i {
            color: var(--el-color-primary);
            right: -8px;
          }
        }
      }
    }
  }
}
</style>
