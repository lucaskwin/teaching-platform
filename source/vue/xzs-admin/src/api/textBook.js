import { post } from '@/utils/request'

export default {
  list: query => post('/api/admin/education/textbook/list'),
  pageList: query => post('/api/admin/education/textbook/page', query),
  edit: query => post('/api/admin/education/textbook/edit', query),
  select: id => post('/api/admin/education/textbook/select/' + id),
  deleteTextBook: id => post('/api/admin/education/textbook/delete/' + id)



}
