
import { ElMessageBox } from 'element-plus'

const confirm = (message, okfun) => {
    ElMessageBox.confirm(message, 'reminder', {
        confirmButtonText: 'confirm',
        cancelButtonText: 'cancel',
        type: 'info',
    }).then(() => {
        okfun();
    }).catch(() => { })
};

export default confirm;


