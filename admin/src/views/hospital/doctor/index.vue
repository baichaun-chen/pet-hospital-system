<template>
    <div class="app-container">
        <!-- 顶部搜索 -->
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="姓名" prop="name">
                <el-input
                        v-model="queryParams.name"
                        placeholder="请输入姓名"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="职称" prop="title">
                <el-input
                        v-model="queryParams.title"
                        placeholder="请输入职称"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="擅长" prop="specialty">
                <el-input
                        v-model="queryParams.specialty"
                        placeholder="请输入擅长"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <!-- 顶部按钮 -->
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        plain
                        icon="Plus"
                        @click="handleAdd"
                        v-hasPermi="['hospital:doctor:add']"
                >新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        plain
                        icon="Edit"
                        :disabled="single"
                        @click="handleUpdate"
                        v-hasPermi="['hospital:doctor:edit']"
                >修改
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="Delete"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['hospital:doctor:remove']"
                >删除
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['hospital:doctor:export']"
                >导出
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格 -->
        <el-table @row-click="clickRow" ref="table" highlight-current-row
                  border v-loading="loading" :data="doctorList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
            <el-table-column label="姓名" align="center" prop="name"/>
            <el-table-column label="职称" align="center" prop="title"/>
            <el-table-column label="擅长" align="center" prop="specialty"/>
            <el-table-column label="照片" align="center" prop="photo" width="100">
                <template #default="scope">
                    <image-preview :src="scope.row.photo" :width="50" :height="50"/>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                               v-hasPermi="['hospital:doctor:edit']">修改
                    </el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                               v-hasPermi="['hospital:doctor:remove']">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <pagination
                v-show="total>0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
        />

        <!-- 添加或修改医生对话框 -->
        <vxe-modal :title="title" v-model="open" width="500px" show-maximize showFooter resize>
            <el-form ref="doctorRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="form.name" placeholder="请输入姓名"/>
                </el-form-item>
                <el-form-item label="职称" prop="title">
                    <el-input v-model="form.title" placeholder="请输入职称"/>
                </el-form-item>
                <el-form-item label="擅长" prop="specialty">
                    <el-input v-model="form.specialty" placeholder="请输入擅长"/>
                </el-form-item>
                <el-form-item label="照片" prop="photo">
                    <image-upload v-model="form.photo"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </vxe-modal>
    </div>
</template>

<script setup name="Doctor">
import {listDoctor, getDoctor, delDoctor, addDoctor, updateDoctor} from "@/api/hospital/doctor"
import {getToken} from "@/utils/auth.js";

const baseURL = import.meta.env.VITE_APP_BASE_API

const {proxy} = getCurrentInstance()

const doctorList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const selectedRow = ref(null)

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        title: null,
        specialty: null,
    },
    rules: {
        name: [
            {required: true, message: "姓名不能为空", trigger: "blur"}
        ],
        title: [
            {required: true, message: "职称不能为空", trigger: "blur"}
        ],
        specialty: [
            {required: true, message: "擅长不能为空", trigger: "blur"}
        ],
        photo: [
            {required: true, message: "照片不能为空", trigger: "blur"}
        ],
    }
})

const {queryParams, form, rules} = toRefs(data)

//点击行 获取行
const clickRow = (row) => {
    selectedRow.value = row; // 更新选中的行
    const table = proxy.$refs.table;
    // 清除所有已选中的行
    table.clearSelection();
    // 选中当前点击的行
    table.toggleRowSelection(row, true);
}

/** 自定义序号 */
const indexMethod = (index) => {
    let pageNum = queryParams.value.pageNum - 1;
    if ((pageNum !== -1 && pageNum !== 0)) {
        return (index + 1) + (pageNum * queryParams.value.pageSize);
    } else {
        return (index + 1)
    }
}

/** 查询医生列表 */
const getList = () => {
    loading.value = true
    listDoctor(queryParams.value).then(response => {
        doctorList.value = response.rows
        total.value = response.total
        loading.value = false
    })
}

// 取消按钮
const cancel = () => {
    open.value = false
    reset()
}

// 表单重置
const reset = () => {
    form.value = {
        doctorId: null,
        name: null,
        title: null,
        specialty: null,
        photo: null,
        createTime: null
    }
    proxy.resetForm("doctorRef")
}

/** 搜索按钮操作 */
const handleQuery = () => {
    queryParams.value.pageNum = 1
    getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
    proxy.resetForm("queryRef")
    handleQuery()
}

// 多选框选中数据
const handleSelectionChange = (selection) => {
    ids.value = selection.map(item => item.doctorId)
    single.value = selection.length != 1
    multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
    reset()
    open.value = true
    title.value = "添加医生"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
    reset()
    const _doctorId = row.doctorId || ids.value
    getDoctor(_doctorId).then(response => {
        form.value = response.data
        open.value = true
        title.value = "修改医生"
    })
}

/** 提交按钮 */
const submitForm = () => {
    proxy.$refs["doctorRef"].validate(valid => {
        if (valid) {
            if (form.value.doctorId != null) {
                updateDoctor(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addDoctor(form.value).then(response => {
                    proxy.$modal.msgSuccess("新增成功")
                    open.value = false
                    getList()
                })
            }
        }
    })
}

/** 删除按钮操作 */
const handleDelete = (row) => {
    const _doctorIds = row.doctorId || ids.value
    proxy.$modal.confirm('是否确认删除该项数据？').then(function () {
        return delDoctor(_doctorIds)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {
    })
}

/** 导出按钮操作 */
const handleExport = () => {
    proxy.download('hospital/doctor/export', {
        ...queryParams.value
    }, `doctor_${new Date().getTime()}.xlsx`)
}

getList()
</script>
