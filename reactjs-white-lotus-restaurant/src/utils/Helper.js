const Helper = {
  formatCurrencyToVND: (number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(Number(number))
  },
  getFilename: (fileUrl) => {
    return fileUrl.split('/').pop().split('.')[0]
  },
  countTime: (timeA, timeB) => {
    let result
    const c = parseInt((timeA - timeB) / (1000 * 60), 10)
    if (c <= 0) {
      result = 'vài giây trước'
    } else if (c < 60) {
      result = `${c} phút trước`
    } else {
      const d = parseInt((timeA - timeB) / (1000 * 60 * 60), 10)
      result = `${d} giờ trước`
    }
    return result
  },
  formatDate: (date) => {
    const yyyy = date.getFullYear();
    let mm = date.getMonth() + 1; // Months start at 0!
    let dd = date.getDate();

    if (dd < 10) dd = '0' + dd;
    if (mm < 10) mm = '0' + mm;

    const formattedToday = dd + '-' + mm + '-' + yyyy;
    return formattedToday
  }
}

export default Helper
