import request from '@/utils/request'
export function getList(data) {
  return request({
    url: '/sys/car/list',
    method: 'get',
    params: data
  })
}

export function getInfo(id) {
  return request({
    url: '/sys/car/info/' + id,
    method: 'get'
  })
}

export function getPayInfo(id) {
  return request({
    url: '/sys/car/payInfo/' + id,
    method: 'get'
  })
}

export function updatePay(data) {
  return request({
    url: '/sys/car/updatePay',
    method: 'put',
    data
  })
}

export function add(data) {
  return request({
    url: '/sys/car/add',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/sys/car/edit',
    method: 'put',
    data
  })
}

export function del(data) {
  return request({
    url: '/sys/car/del',
    method: 'delete',
    data
  })
}

export function parsefile(filename) {
  return request({
    url: '/sys/car/parsefile/' + filename,
    method: 'post'
  })
}
