import request from '@/utils/request'
export function getList(data) {
  return request({
    url: '/sys/pay/list',
    method: 'get',
    params: data
  })
}

export function getInfo(id) {
  return request({
    url: '/sys/pay/info/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/sys/pay/add',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/sys/pay/edit',
    method: 'put',
    data
  })
}

export function del(data) {
  return request({
    url: '/sys/pay/del',
    method: 'delete',
    data
  })
}

export function exportExcel(data) {
  return request({
    url: '/sys/pay/exportExcel',
    method: 'get',
    params: data
  })
}
