/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: C:\\Users\\carlos\\AppData\\Local\\Android\\Sdk\\build-tools\\36.0.0\\aidl.exe -pC:\\Users\\carlos\\AppData\\Local\\Android\\Sdk\\platforms\\android-36\\framework.aidl -oC:\\PROGRAMACION\\PROGRAMACION\\ ANDROID\\qmgcomanderoV3\\app\\build\\generated\\aidl_source_output_dir\\debug\\out -IC:\\PROGRAMACION\\PROGRAMACION\\ ANDROID\\qmgcomanderoV3\\app\\src\\main\\aidl -IC:\\PROGRAMACION\\PROGRAMACION\\ ANDROID\\qmgcomanderoV3\\app\\src\\debug\\aidl -IC:\\Users\\carlos\\.gradle\\caches\\9.3.1\\transforms\\c0cf50ae5bc5f7128971d51cbbf2d55c\\workspace\\transformed\\media-1.0.0\\aidl -IC:\\Users\\carlos\\.gradle\\caches\\9.3.1\\transforms\\7ff7763a1156df6554d0fe107c454b9c\\workspace\\transformed\\core-1.13.0\\aidl -IC:\\Users\\carlos\\.gradle\\caches\\9.3.1\\transforms\\8fa9ea4040d62f30eed16d7942c89054\\workspace\\transformed\\versionedparcelable-1.1.1\\aidl -dC:\\Users\\carlos\\AppData\\Local\\Temp\\aidl14972012349214613025.d C:\\PROGRAMACION\\PROGRAMACION\\ ANDROID\\qmgcomanderoV3\\app\\src\\main\\aidl\\com\\iposprinter\\iposprinterservice\\IPosPrinterCallback.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package com.iposprinter.iposprinterservice;
/** 打印服务执行结果的回调 */
public interface IPosPrinterCallback extends android.os.IInterface
{
  /** Default implementation for IPosPrinterCallback. */
  public static class Default implements com.iposprinter.iposprinterservice.IPosPrinterCallback
  {
    /**
     * 返回执行结果
     * @param isSuccess:	  true执行成功，false 执行失败
     */
    @Override public void onRunResult(boolean isSuccess) throws android.os.RemoteException
    {
    }
    /**
     * 返回结果(字符串数据)
     * @param result:	结果，打印机上电以来打印长度(单位mm)
     */
    @Override public void onReturnString(java.lang.String result) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.iposprinter.iposprinterservice.IPosPrinterCallback
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.iposprinter.iposprinterservice.IPosPrinterCallback interface,
     * generating a proxy if needed.
     */
    public static com.iposprinter.iposprinterservice.IPosPrinterCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.iposprinter.iposprinterservice.IPosPrinterCallback))) {
        return ((com.iposprinter.iposprinterservice.IPosPrinterCallback)iin);
      }
      return new com.iposprinter.iposprinterservice.IPosPrinterCallback.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(descriptor);
      }
      if (code == INTERFACE_TRANSACTION) {
        reply.writeString(descriptor);
        return true;
      }
      switch (code)
      {
        case TRANSACTION_onRunResult:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.onRunResult(_arg0);
          break;
        }
        case TRANSACTION_onReturnString:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.onReturnString(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements com.iposprinter.iposprinterservice.IPosPrinterCallback
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      /**
       * 返回执行结果
       * @param isSuccess:	  true执行成功，false 执行失败
       */
      @Override public void onRunResult(boolean isSuccess) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((isSuccess)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRunResult, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * 返回结果(字符串数据)
       * @param result:	结果，打印机上电以来打印长度(单位mm)
       */
      @Override public void onReturnString(java.lang.String result) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(result);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onReturnString, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onRunResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onReturnString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "com.iposprinter.iposprinterservice.IPosPrinterCallback";
  /**
   * 返回执行结果
   * @param isSuccess:	  true执行成功，false 执行失败
   */
  public void onRunResult(boolean isSuccess) throws android.os.RemoteException;
  /**
   * 返回结果(字符串数据)
   * @param result:	结果，打印机上电以来打印长度(单位mm)
   */
  public void onReturnString(java.lang.String result) throws android.os.RemoteException;
}
