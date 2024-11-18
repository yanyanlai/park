import request from '@/utils/request'
export function getList(data) {
  return request({
    url: '/sys/property/list',
    method: 'get',
    params: data
  })
}

export function getInfo(id) {
  return request({
    url: '/sys/property/info/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/sys/property/add',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/sys/property/edit',
    method: 'put',
    data
  })
}

export function del(data) {
  return request({
    url: '/sys/property/del',
    method: 'delete',
    data
  })
}
