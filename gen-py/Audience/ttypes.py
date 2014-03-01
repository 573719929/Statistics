#
# Autogenerated by Thrift Compiler (0.8.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TException

from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol, TProtocol
try:
  from thrift.protocol import fastbinary
except:
  fastbinary = None



class Website:
  """
  Attributes:
   - wid
   - name
   - domain_name
   - is_working
   - create_time
   - code
   - uid
  """

  thrift_spec = (
    None, # 0
    (1, TType.I32, 'wid', None, None, ), # 1
    (2, TType.STRING, 'name', None, None, ), # 2
    (3, TType.STRING, 'domain_name', None, None, ), # 3
    (4, TType.BOOL, 'is_working', None, None, ), # 4
    (5, TType.STRING, 'create_time', None, None, ), # 5
    (6, TType.STRING, 'code', None, None, ), # 6
    (7, TType.I32, 'uid', None, None, ), # 7
  )

  def __init__(self, wid=None, name=None, domain_name=None, is_working=None, create_time=None, code=None, uid=None,):
    self.wid = wid
    self.name = name
    self.domain_name = domain_name
    self.is_working = is_working
    self.create_time = create_time
    self.code = code
    self.uid = uid

  def read(self, iprot):
    if iprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None and fastbinary is not None:
      fastbinary.decode_binary(self, iprot.trans, (self.__class__, self.thrift_spec))
      return
    iprot.readStructBegin()
    while True:
      (fname, ftype, fid) = iprot.readFieldBegin()
      if ftype == TType.STOP:
        break
      if fid == 1:
        if ftype == TType.I32:
          self.wid = iprot.readI32();
        else:
          iprot.skip(ftype)
      elif fid == 2:
        if ftype == TType.STRING:
          self.name = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 3:
        if ftype == TType.STRING:
          self.domain_name = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 4:
        if ftype == TType.BOOL:
          self.is_working = iprot.readBool();
        else:
          iprot.skip(ftype)
      elif fid == 5:
        if ftype == TType.STRING:
          self.create_time = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 6:
        if ftype == TType.STRING:
          self.code = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 7:
        if ftype == TType.I32:
          self.uid = iprot.readI32();
        else:
          iprot.skip(ftype)
      else:
        iprot.skip(ftype)
      iprot.readFieldEnd()
    iprot.readStructEnd()

  def write(self, oprot):
    if oprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and self.thrift_spec is not None and fastbinary is not None:
      oprot.trans.write(fastbinary.encode_binary(self, (self.__class__, self.thrift_spec)))
      return
    oprot.writeStructBegin('Website')
    if self.wid is not None:
      oprot.writeFieldBegin('wid', TType.I32, 1)
      oprot.writeI32(self.wid)
      oprot.writeFieldEnd()
    if self.name is not None:
      oprot.writeFieldBegin('name', TType.STRING, 2)
      oprot.writeString(self.name)
      oprot.writeFieldEnd()
    if self.domain_name is not None:
      oprot.writeFieldBegin('domain_name', TType.STRING, 3)
      oprot.writeString(self.domain_name)
      oprot.writeFieldEnd()
    if self.is_working is not None:
      oprot.writeFieldBegin('is_working', TType.BOOL, 4)
      oprot.writeBool(self.is_working)
      oprot.writeFieldEnd()
    if self.create_time is not None:
      oprot.writeFieldBegin('create_time', TType.STRING, 5)
      oprot.writeString(self.create_time)
      oprot.writeFieldEnd()
    if self.code is not None:
      oprot.writeFieldBegin('code', TType.STRING, 6)
      oprot.writeString(self.code)
      oprot.writeFieldEnd()
    if self.uid is not None:
      oprot.writeFieldBegin('uid', TType.I32, 7)
      oprot.writeI32(self.uid)
      oprot.writeFieldEnd()
    oprot.writeFieldStop()
    oprot.writeStructEnd()

  def validate(self):
    return


  def __repr__(self):
    L = ['%s=%r' % (key, value)
      for key, value in self.__dict__.iteritems()]
    return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

  def __eq__(self, other):
    return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

  def __ne__(self, other):
    return not (self == other)

class Crowd:
  """
  Attributes:
   - cid
   - name
   - status
   - number
   - valid_time
   - create_time
   - wid
  """

  thrift_spec = (
    None, # 0
    (1, TType.I32, 'cid', None, None, ), # 1
    (2, TType.STRING, 'name', None, None, ), # 2
    (3, TType.I32, 'status', None, None, ), # 3
    (4, TType.I32, 'number', None, None, ), # 4
    (5, TType.I32, 'valid_time', None, None, ), # 5
    (6, TType.STRING, 'create_time', None, None, ), # 6
    (7, TType.I32, 'wid', None, None, ), # 7
  )

  def __init__(self, cid=None, name=None, status=None, number=None, valid_time=None, create_time=None, wid=None,):
    self.cid = cid
    self.name = name
    self.status = status
    self.number = number
    self.valid_time = valid_time
    self.create_time = create_time
    self.wid = wid

  def read(self, iprot):
    if iprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None and fastbinary is not None:
      fastbinary.decode_binary(self, iprot.trans, (self.__class__, self.thrift_spec))
      return
    iprot.readStructBegin()
    while True:
      (fname, ftype, fid) = iprot.readFieldBegin()
      if ftype == TType.STOP:
        break
      if fid == 1:
        if ftype == TType.I32:
          self.cid = iprot.readI32();
        else:
          iprot.skip(ftype)
      elif fid == 2:
        if ftype == TType.STRING:
          self.name = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 3:
        if ftype == TType.I32:
          self.status = iprot.readI32();
        else:
          iprot.skip(ftype)
      elif fid == 4:
        if ftype == TType.I32:
          self.number = iprot.readI32();
        else:
          iprot.skip(ftype)
      elif fid == 5:
        if ftype == TType.I32:
          self.valid_time = iprot.readI32();
        else:
          iprot.skip(ftype)
      elif fid == 6:
        if ftype == TType.STRING:
          self.create_time = iprot.readString();
        else:
          iprot.skip(ftype)
      elif fid == 7:
        if ftype == TType.I32:
          self.wid = iprot.readI32();
        else:
          iprot.skip(ftype)
      else:
        iprot.skip(ftype)
      iprot.readFieldEnd()
    iprot.readStructEnd()

  def write(self, oprot):
    if oprot.__class__ == TBinaryProtocol.TBinaryProtocolAccelerated and self.thrift_spec is not None and fastbinary is not None:
      oprot.trans.write(fastbinary.encode_binary(self, (self.__class__, self.thrift_spec)))
      return
    oprot.writeStructBegin('Crowd')
    if self.cid is not None:
      oprot.writeFieldBegin('cid', TType.I32, 1)
      oprot.writeI32(self.cid)
      oprot.writeFieldEnd()
    if self.name is not None:
      oprot.writeFieldBegin('name', TType.STRING, 2)
      oprot.writeString(self.name)
      oprot.writeFieldEnd()
    if self.status is not None:
      oprot.writeFieldBegin('status', TType.I32, 3)
      oprot.writeI32(self.status)
      oprot.writeFieldEnd()
    if self.number is not None:
      oprot.writeFieldBegin('number', TType.I32, 4)
      oprot.writeI32(self.number)
      oprot.writeFieldEnd()
    if self.valid_time is not None:
      oprot.writeFieldBegin('valid_time', TType.I32, 5)
      oprot.writeI32(self.valid_time)
      oprot.writeFieldEnd()
    if self.create_time is not None:
      oprot.writeFieldBegin('create_time', TType.STRING, 6)
      oprot.writeString(self.create_time)
      oprot.writeFieldEnd()
    if self.wid is not None:
      oprot.writeFieldBegin('wid', TType.I32, 7)
      oprot.writeI32(self.wid)
      oprot.writeFieldEnd()
    oprot.writeFieldStop()
    oprot.writeStructEnd()

  def validate(self):
    return


  def __repr__(self):
    L = ['%s=%r' % (key, value)
      for key, value in self.__dict__.iteritems()]
    return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

  def __eq__(self, other):
    return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

  def __ne__(self, other):
    return not (self == other)