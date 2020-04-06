package com.danhdueexoictif.androidgenericadapter.data.remote

import com.danhdueexoictif.androidgenericadapter.BuildConfig
import com.danhdueexoictif.androidgenericadapter.data.remote.response.HttpResponseCode.HTTP_OK
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * This will help us to test our networking code while a particular API is not implemented
 * yet on Backend side.
 */
class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri().toString()
            val responseString = when {
                uri.endsWith("sample?lang=&page=1") -> getListOfSampleData
                else -> ""
            }

            return if (responseString.isNotEmpty()) {
                chain.proceed(chain.request())
                    .newBuilder()
                    .code(HTTP_OK)
                    .protocol(Protocol.HTTP_2)
                    .message(responseString)
                    .body(
                        responseString.toByteArray()
                            .toResponseBody("application/json".toMediaTypeOrNull())
                    )
                    .addHeader("content-type", "application/json")
                    .build()
            } else {
                chain.proceed(chain.request())
            }
        } else {
            //just to be on safe side.
            throw IllegalAccessError(
                "MockInterceptor is only meant for Testing Purposes and " +
                        "bound to be used only with DEBUG mode"
            )
        }
    }

}

const val getListOfSampleData = """
{
  "data": [
    {
      "id": 1,
      "avatar": "https://img.freepik.com/free-vector/icons-big-data-background_23-2148006396.jpg",
      "userName": "Nguyễn Hữu Công",
      "unit": "Android Developer - Unit 2",
      "quote": "Hi vọng trong thời gian đồng hành cùng Sun*, tôi sẽ cùng Sun* tạo ra những sản phẩm sáng tạo, xây dựng môi trường ..."
    },
    {
      "id": 2,
      "avatar": "https://img.freepik.com/free-vector/icons-big-data-background_23-2148006396.jpg",
      "userName": "Nguyễn Thị Thu Thảo",
      "unit": "Copywriter - HR Unit",
      "quote": "Trước mắt, khi trở thành nhân viên của Bộ phận Truyền thông nội bộ, tôi muốn đóng góp chất xám của mình để bộ phận …."
    },
    {
      "id": 3,
      "avatar": "https://img.freepik.com/free-vector/icons-big-data-background_23-2148006396.jpg",
      "userName": "Nguyễn Trung Thành",
      "unit": "PHP Developer - R&D Unit",
      "quote": "Mình muốn tạo ra nhiều hơn sự vui vẻ và hạnh phúc cho những người xung quanh, khi mỗi hạt nhân trong ..."
    },
    {
      "id": 4,
      "avatar": "https://img.freepik.com/free-vector/icons-big-data-background_23-2148006396.jpg",
      "userName": "Nguyễn Hữu Công",
      "unit": "Android Developer - Unit 2",
      "quote": "Hi vọng trong thời gian đồng hành cùng Sun*, tôi sẽ cùng Sun* tạo ra những sản phẩm sáng tạo, xây dựng môi trường ..."
    },
    {
      "id": 5,
      "avatar": "https://img.freepik.com/free-vector/icons-big-data-background_23-2148006396.jpg",
      "userName": "Nguyễn Hữu Công",
      "unit": "Android Developer - Unit 2",
      "quote": "Hi vọng trong thời gian đồng hành cùng Sun*, tôi sẽ cùng Sun* tạo ra những sản phẩm sáng tạo, xây dựng môi trường ..."
    },
    {
      "id": 6,
      "avatar": "https://img.freepik.com/free-vector/icons-big-data-background_23-2148006396.jpg",
      "userName": "Nguyễn Hữu Công",
      "unit": "Android Developer - Unit 2",
      "quote": "Hi vọng trong thời gian đồng hành cùng Sun*, tôi sẽ cùng Sun* tạo ra những sản phẩm sáng tạo, xây dựng môi trường ..."
    },
    {
      "id": 7,
      "avatar": "https://img.freepik.com/free-vector/icons-big-data-background_23-2148006396.jpg",
      "userName": "Nguyễn Hữu Công",
      "unit": "Android Developer - Unit 2",
      "quote": "Hi vọng trong thời gian đồng hành cùng Sun*, tôi sẽ cùng Sun* tạo ra những sản phẩm sáng tạo, xây dựng môi trường ..."
    }
  ],
  "error": {
    "code": 123,
    "message": "DanhDue ExOICIF"
  }
}
"""
