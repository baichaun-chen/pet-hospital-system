//组件
import App from './App'
import store from './store' // store
import directive from './directive' // directive
import plugins from './plugins' // plugins
import './permission' // permission
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels, tansParams, uniappRules } from '@/utils/huacai'

import {
	useDict
} from '@/utils/dict'


//客户端渲染
import {
	createApp as createVueApp
} from 'vue'

//服务器端渲染
// import {
// 	createSSRApp as createVueApp
// } from 'vue'

export function createApp() {
	const app = createVueApp(App)
	app.config.globalProperties.useDict = useDict
	app.config.globalProperties.parseTime = parseTime
	app.config.globalProperties.resetForm = resetForm
	app.config.globalProperties.handleTree = handleTree
	app.config.globalProperties.addDateRange = addDateRange
	app.config.globalProperties.selectDictLabel = selectDictLabel
	app.config.globalProperties.selectDictLabels = selectDictLabels
	app.config.globalProperties.tansParams = tansParams
	app.config.globalProperties.uniappRules = uniappRules
	app.use(store)
	app.use(plugins)
	// app.use(directive)
	directive(app)
	return {app}
}
