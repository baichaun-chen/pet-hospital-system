<template>
    <div class="app-container">
        <!-- 顶部搜索 -->
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="预约类型" prop="type">
                <el-select style="width: 193px" v-model="queryParams.type" placeholder="请选择预约类型" clearable>
                    <el-option
                            v-for="dict in reservation_type"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="预约状态" prop="status">
                <el-select style="width: 193px" v-model="queryParams.status" placeholder="请选择预约状态" clearable>
                    <el-option
                            v-for="dict in reservation_status"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="宠物名字" prop="petName">
                <el-input
                        v-model="queryParams.petName"
                        placeholder="请输入宠物名字"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="医生姓名" prop="name">
                <el-input
                        v-model="queryParams.name"
                        placeholder="请输入医生姓名"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="疫苗名称" prop="vaccineName">
                <el-input
                        v-model="queryParams.vaccineName"
                        placeholder="请输入疫苗名称"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="预约用户" prop="userName">
                <el-input
                        v-model="queryParams.userName"
                        placeholder="请输入预约用户"
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
                        v-hasPermi="['hospital:reservation:add']"
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
                        v-hasPermi="['hospital:reservation:edit']"
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
                        v-hasPermi="['hospital:reservation:remove']"
                >删除
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['hospital:reservation:export']"
                >导出
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        icon="Upload"
                        size="mini"
                        @click="handleImport"
                >导入
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格 -->
        <el-table @row-click="clickRow" ref="table" highlight-current-row
                  border v-loading="loading" :data="reservationList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
            <el-table-column label="预约单号" align="center" prop="reservationId" width="190"/>
            <el-table-column label="预约类型" align="center" prop="type">
                <template #default="scope">
                    <dict-tag :options="reservation_type" :value="scope.row.type"/>
                </template>
            </el-table-column>
            <el-table-column label="预约状态" align="center" prop="status">
                <template #default="scope">
                    <dict-tag :options="reservation_status" :value="scope.row.status"/>
                </template>
            </el-table-column>
            <el-table-column label="宠物名字" align="center" prop="petName"/>
            <el-table-column label="宠物品种" align="center" prop="petBread"/>
            <el-table-column label="宠物年龄" align="center" prop="petAge"/>
            <el-table-column label="症状" align="center" prop="symptom" show-overflow-tooltip/>
            <el-table-column label="预约日期" align="center" prop="date"/>
            <el-table-column label="预约时间" align="center" prop="time"/>
            <el-table-column label="医生姓名" align="center" prop="name"/>
            <el-table-column label="职称" align="center" prop="title"/>
            <el-table-column label="擅长" align="center" prop="specialty"/>
            <el-table-column label="照片" align="center" prop="photo" width="100">
                <template #default="scope">
                    <image-preview :src="scope.row.photo" :width="50" :height="50"/>
                </template>
            </el-table-column>
            <el-table-column label="疫苗名称" align="center" prop="vaccineName"/>
            <el-table-column label="预约用户" align="center" prop="userName"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="110">
                <template #default="scope">
                    <el-button link type="success" v-if="scope.row.status === '待就诊'"
                               icon="Edit" @click="handleOk(scope.row)"
                    >完成预约单
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

        <!-- 导入对话框 -->
        <vxe-modal :title="upload.title" v-model="upload.open" width="400px" showFooter show-zoom resize>
            <el-upload
                    ref="uploadRef"
                    :limit="1"
                    accept=".xlsx, .xls"
                    :headers="upload.headers"
                    :action="upload.url"
                    :data="{ updateSupport: upload.updateSupport }"
                    :disabled="upload.isUploading"
                    :on-progress="handleFileUploadProgress"
                    :on-success="handleFileSuccess"
                    :auto-upload="false"
                    drag
            >
                <el-icon class="el-icon--upload">
                    <upload-filled/>
                </el-icon>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <template #tip>
                    <div class="el-upload__tip text-center">
                        <span>仅允许导入xls、xlsx格式文件。</span>
                        <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
                                 @click="importTemplate">下载模板
                        </el-link>
                    </div>
                </template>
            </el-upload>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFileForm">确 定</el-button>
                <el-button @click="upload.open = false">取 消</el-button>
            </div>
        </vxe-modal>

        <!-- 添加或修改预约对话框 -->
        <vxe-modal :title="title" v-model="open" width="500px" show-maximize showFooter resize>
            <el-form ref="reservationRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="预约类型" prop="type">
                    <el-select v-model="form.type" placeholder="请选择预约类型">
                        <el-option
                                v-for="dict in reservation_type"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="宠物名字" prop="petName">
                    <el-input v-model="form.petName" placeholder="请输入宠物名字"/>
                </el-form-item>
                <el-form-item label="宠物品种" prop="petBread">
                    <el-input v-model="form.petBread" placeholder="请输入宠物品种"/>
                </el-form-item>
                <el-form-item label="宠物年龄" prop="petAge">
                    <el-input v-model="form.petAge" placeholder="请输入宠物年龄"/>
                </el-form-item>
                <el-form-item label="症状" prop="symptom">
                    <el-input v-model="form.symptom" type="textarea" placeholder="请输入内容"/>
                </el-form-item>
                <el-form-item label="预约日期" prop="date">
                    <el-input v-model="form.date" placeholder="请输入预约日期"/>
                </el-form-item>
                <el-form-item label="预约时间" prop="time">
                    <el-input v-model="form.time" placeholder="请输入预约时间"/>
                </el-form-item>
                <el-form-item label="医生ID" prop="doctorId">
                    <el-input v-model="form.doctorId" placeholder="请输入医生ID"/>
                </el-form-item>
                <el-form-item label="疫苗ID" prop="vaccineId">
                    <el-input v-model="form.vaccineId" placeholder="请输入疫苗ID"/>
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

<script setup name="Reservation">
import {
    listReservation,
    getReservation,
    delReservation,
    addReservation,
    updateReservation
} from "@/api/hospital/reservation"
import {getToken} from "@/utils/auth.js";
import {ElMessage, ElMessageBox} from "element-plus";

const baseURL = import.meta.env.VITE_APP_BASE_API

const {proxy} = getCurrentInstance()
const {reservation_status, reservation_type} = proxy.useDict('reservation_status', 'reservation_type')

const reservationList = ref([])
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
        type: null,
        status: null,
        petName: null,
        doctorId: null,
        vaccineId: null,
        userId: null,
        name: null,
        vaccineName: null,
        userName: null,
    },
    rules: {
        type: [
            {required: true, message: "预约类型不能为空", trigger: "change"}
        ],
        status: [
            {required: true, message: "预约状态不能为空", trigger: "change"}
        ],
        petName: [
            {required: true, message: "宠物名字不能为空", trigger: "blur"}
        ],
        petBread: [
            {required: true, message: "宠物品种不能为空", trigger: "blur"}
        ],
        petAge: [
            {required: true, message: "宠物年龄不能为空", trigger: "blur"}
        ],
        symptom: [
            {required: true, message: "症状不能为空", trigger: "blur"}
        ],
        date: [
            {required: true, message: "预约日期不能为空", trigger: "blur"}
        ],
        time: [
            {required: true, message: "预约时间不能为空", trigger: "blur"}
        ],
        userId: [
            {required: true, message: "用户ID不能为空", trigger: "blur"}
        ],
    },
    // 导入参数
    upload: {
        // 是否显示弹出层（导入）
        open: false,
        // 弹出层标题（导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: baseURL + "/hospital/reservation/importData"
    }
})

const {queryParams, form, rules, upload} = toRefs(data)

//完成预约
const handleOk = (row) => {
    ElMessageBox.confirm(
        '确定这个预约单已经完成了吗?',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            const item = {
                reservationId: row.reservationId,
                status: '已完成'
            }
            updateReservation(item).then(res => {
                getList()
                ElMessage({type: 'success', message: '预约单已完成',})
            })
        })
}

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

/** 查询预约列表 */
const getList = () => {
    loading.value = true
    listReservation(queryParams.value).then(response => {
        reservationList.value = response.rows
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
        reservationId: null,
        type: null,
        status: null,
        petName: null,
        petBread: null,
        petAge: null,
        symptom: null,
        date: null,
        time: null,
        doctorId: null,
        vaccineId: null,
        userId: null,
        createTime: null
    }
    proxy.resetForm("reservationRef")
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
    ids.value = selection.map(item => item.reservationId)
    single.value = selection.length != 1
    multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
    reset()
    open.value = true
    title.value = "添加预约"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
    reset()
    const _reservationId = row.reservationId || ids.value
    getReservation(_reservationId).then(response => {
        form.value = response.data
        open.value = true
        title.value = "修改预约"
    })
}

/** 提交按钮 */
const submitForm = () => {
    proxy.$refs["reservationRef"].validate(valid => {
        if (valid) {
            if (form.value.reservationId != null) {
                updateReservation(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addReservation(form.value).then(response => {
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
    const _reservationIds = row.reservationId || ids.value
    proxy.$modal.confirm('是否确认删除该项数据？').then(function () {
        return delReservation(_reservationIds)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {
    })
}

/** 导出按钮操作 */
const handleExport = () => {
    proxy.download('hospital/reservation/export', {
        ...queryParams.value
    }, `reservation_${new Date().getTime()}.xlsx`)
}

/** 下载模板操作 */
const importTemplate = () => {
    proxy.download('hospital/reservation/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
}

/** 导入按钮操作 */
const handleImport = () => {
    upload.value.title = "预约导入";
    upload.value.open = true;
}

// 文件上传中处理
const handleFileUploadProgress = (event, file, fileList) => {
    upload.value.isUploading = true;
}

// 文件上传成功处理
const handleFileSuccess = (response, file, fileList) => {
    upload.value.open = false;
    upload.value.isUploading = false;
    proxy.$refs.uploadRef.clearFiles();
    proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
    getList();
}

// 提交上传文件
const submitFileForm = () => {
    proxy.$refs.uploadRef.submit();
}

getList()
</script>
