import request from '@/utils/request'

// 查询总用户数 预约数 医生数 疫苗数
export function selectHomeCount() {
    return request({
        url: '/home/page/selectHomeCount',
        method: 'get'
    })
}

// 查询近7天预约统计数据
export function selectReservationStat() {
    return request({
        url: '/home/page/selectReservationStat',
        method: 'get'
    })
}

// 预约状态统计
export function selectReservationStatusChart() {
    return request({
        url: '/home/page/selectReservationStatusChart',
        method: 'get'
    })
}
