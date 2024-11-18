import request from '@/utils/request'
export function getList(data) {
  return request({
    url: '/sys/park/list',
    method: 'get',
    params: data
  })
}

export function getInfo(id) {
  return request({
    url: '/sys/park/info/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/sys/park/add',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/sys/park/edit',
    method: 'put',
    data
  })
}

export function del(data) {
  return request({
    url: '/sys/park/del',
    method: 'delete',
    data
  })
}

export function getParkMap(data) {
  return request({
    url: '/sys/park/getParkMap',
    method: 'get'
  })
}
