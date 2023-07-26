import React from 'react'
import 'features/Home/loading/Loading.scss'

import Images from 'constants/images'

function Loading() {
  return (
    <div className="bg-loading hide">
      <div className="lds-ring">
        <div></div>
        <div></div>
        <div></div>
      </div>
      <img
        src={Images.LOADING_LOGO}
        alt="logo"
        className="loading-logo"
      />
    </div>
  )
}
export default Loading
