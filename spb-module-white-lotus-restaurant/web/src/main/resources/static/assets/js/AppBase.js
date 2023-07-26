class AppBase {
    static DOMAIN_SERVER = location.origin;

    static API_SERVER = this.DOMAIN_SERVER + '/api';

    static API_AUTH = this.API_SERVER + '/auth';

    static API_PRODUCT = this.API_SERVER + '/products';


    static API_STAFF= this.API_SERVER + '/staffs';

    static API_ORDER= this.API_SERVER + '/orders';

    static API_BILL= this.API_SERVER + '/bills';

    static API_TABLE = this.API_SERVER + '/tables';


    static API_CUSTOMER = this.API_SERVER + '/customers';

    static API_PROVINCE = "https://vapi.vnappmob.com/api/province/";

    static API_DISTRICT = this.API_PROVINCE + "district/";

    static API_WARD = this.API_PROVINCE + "ward/";

    static ROLE_API = this.DOMAIN_SERVER + "/api/roles";

    static  REPORT_API = this.API_SERVER + "/report"


    static BASE_URL_CLOUD_IMAGE = "https://res.cloudinary.com/dghrt3jel/image/upload";
    static SCALE_IMAGE_W60_H50_Q100 = "c_limit,w_60,h_50,q_100";
    static SCALE_IMAGE_W100_H80_Q100 = "c_limit,w_100,h_80,q_100";
    static SCALE_IMAGE_W600_H850_Q100 = "c_limit,w_600,h_850,q_100";
    static SCALE_IMAGE_W250_H200_Q100 = "c_limit,w_250,h_200,q_100";
    static SCALE_IMAGE_W200_H250_Q100 = "c_limit,w_200,h_250,q_100";

    static API_CLOUDINARY = 'https://res.cloudinary.com/cloudinarymen/image/upload';

    static SCALE_IMAGE_W_90_H_90_Q_100 = 'c_limit,w_90,h_90,q_100';

    static SweetAlert = class {
        static showDeactivateConfirmDialog() {
            return Swal.fire({
                icon: 'warning',
                text: 'Are you sure to deactivate the selected customer ?',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, please deactivate this client !',
                cancelButtonText: 'Cancel',
            })
        }

        static showSuccessAlert(t) {
            Swal.fire({
                icon: 'success',
                title: t,
                position: 'top-end',
                showConfirmButton: false,
                timer: 1500
            })
        }

        static showErrorAlert(t) {
            Swal.fire({
                icon: 'error',
                title: 'Warning',
                text: t,
            })
        }

        static showError401() {
            Swal.fire({
                icon: 'error',
                title: 'Access Denied',
                text: 'Invalid credentials!',
            })
        }

        static showError403() {
            Swal.fire({
                icon: 'error',
                title: 'Access Denied',
                text: 'Bạn không được phép thực hiện chức năng này!',
            })
        }

        static showError500() {
            Swal.fire({
                icon: 'error',
                title: 'Internal Server Error',
                text: 'Hệ thống Server đang có vấn đề hoặc không truy cập được.',
            })
        }
    }
    static IziToast = class {
        static showSuccessAlertLeft(m) {
            iziToast.success({
                title: 'OK',
                position: 'topLeft',
                timeout: 2500,
                message: m
            });
        }

        static showSuccessAlertRight(m) {
            this.iziToast.success({
                title: 'OK',
                position: 'topRight',
                timeout: 2500,
                message: m
            });
        }

        static showErrorAlertLeft(m) {
            iziToast.error({
                title: 'Error',
                position: 'topLeft',
                timeout: 2500,
                message: m
            });
        }

        static showErrorAlertRight(m) {
            iziToast.error({
                title: 'Error',
                position: 'topRight',
                timeout: 2500,
                message: m
            });
        }

    }
}


class ProductAvatar {
    constructor(id, fileName, fileFolder, fileUrl, fileType, cloudId, ts) {
        this.id = id;
        this.fileName = fileName;
        this.fileFolder = fileFolder;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.cloudId = cloudId;
        this.ts = ts;
    }
}
class Product {

    constructor(id, title, price, unit, category, description, productAvatar) {
        this.id = id;
        this.title= title;
        this.price = price;
        this.unit = unit;
        this.category = category;
        this.description = description;
        this.productAvatar = productAvatar;
    }
}

class LocationRegion {
    constructor(id, provinceId, provinceName, districtId, districtName, wardId, wardName, address) {
        this.id = id;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.address = address;
    }
}

class Role {
    constructor(id, code) {
        this.id = id;
        this.code = code;
    }
}

class User {
    constructor(id, username, password, role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}

class StaffAvatar {
    constructor(id, fileName, fileFolder, fileUrl, fileType, cloudId, ts) {
        this.id = id;
        this.fileName = fileName;
        this.fileFolder = fileFolder;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.cloudId = cloudId;
        this.ts = ts;
    }
}

class Staff {
    constructor(id, fullName, phone, email, locationRegion,dob, idAvatar,fileName,fileFolder,fileUrl, role) {
        this.id = id;
        this.fullName = fullName;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.locationRegion = locationRegion;
        this.idAvatar = idAvatar;
        this.fileName = fileName;
        this.fileFolder = fileFolder;
        this.fileUrl = fileUrl;
        this.role = role
    }
}

class CustomerAvatar {
    constructor(id, fileName, fileFolder, fileUrl, fileType, cloudId, ts) {
        this.id = id;
        this.fileName = fileName;
        this.fileFolder = fileFolder;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.cloudId = cloudId;
        this.ts = ts;
    }
}

class Customer {
    constructor(id, fullName, phone, user, locationRegion, customerAvatar) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.user = user;
        this.locationRegion = locationRegion;
        this.customerAvatar = customerAvatar;
    }
}

class ProductFilterDTO {
    constructor(id, keyWord, categoryIdFilter, unitFilter) {
        this.id = id;
        this.keyWord = keyWord;
        this.categoryIdFilter = categoryIdFilter;
        this.unitFilter = unitFilter;
    }
}

class Category {
    constructor(id,title) {
        this.id = id;
        this.title = title;
    }
}

class Unit {
    constructor(id,title) {
        this.id = id;
        this.title = title;
    }
}

class TableDTO{
    constructor(id, name, status, statusValue) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.statusValue = statusValue;
    }
}

