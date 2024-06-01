import { post } from '@/utils/request'

export default {
  select: id => post('/api/admin/threedviewer/select/' + id)
}
