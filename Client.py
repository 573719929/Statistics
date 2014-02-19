#!/usr/bin/env python
# -*- coding : utsf8 -*-

import sys, json, random, traceback
sys.path.append('./gen-py')
DEFAULT_HOST, DEFAULT_PORT = None, int(sys.argv[7])
DEFAULT_HOST, DEFAULT_PORT = '10.160.55.249', 12545
from DataReport import ReportService
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
from thrift import Thrift
from DataReport.ttypes import *
try:
    transport = TTransport.TBufferedTransport(TSocket.TSocket(DEFAULT_HOST, DEFAULT_PORT))
    client = ReportService.Client(TBinaryProtocol.TBinaryProtocol(transport))
    
    transport.open()
    
    q, p = queryOptions(), pageOptions()
    
    q.id = sys.argv[1]
    q.startAt = sys.argv[2]
    q.endAt = sys.argv[3]
    
    p.pageNumber, p.pageSize = int(sys.argv[4]), int(sys.argv[5])
    
    functions = {
        'AdReportByAdid' : client.AdReportByAdid,
        'AdReportByGroupId' : client.AdReportByGroupId,
        'GroupReportByPlanId' : client.GroupReportByPlanId,
        'PlanReportByUid' : client.PlanReportByUid,
        'AreaByAdid' : client.AreaByAdid,
        'AreaByGid' : client.AreaByGid,
        'AreaByPid' : client.AreaByPid,
        'AreaByUid' : client.AreaByUid,
        'DayByAdid' : client.DayByAdid,
        'DayByGid' : client.DayByGid,
        'DayByPid' : client.DayByPid,
        'DayByUid' : client.DayByUid,
        'SourceByAdid' : client.SourceByAdid,
        'SourceByGid' : client.SourceByGid,
        'SourceByPid' : client.SourceByPid,
        'SourceByUid' : client.SourceByUid,
        'HourByAdid' : client.HourByAdid,
        'HourByGid' : client.HourByGid,
        'HourByPid' : client.HourByPid,
        'HourByUid' : client.HourByUid,
        'HostByAdid' : client.HostByAdid,
        'HostByGid' : client.HostByGid,
        'HostByPid' : client.HostByPid,
        'HostByUid' : client.HostByUid,
        'AdspaceByAdid' : client.AdspaceByAdid,
        'AdspaceByGid' : client.AdspaceByGid,
        'AdspaceByPid' : client.AdspaceByPid,
        'AdspaceByUid' : client.AdspaceByUid,
    }
    foobar = functions[sys.argv[6]]
    print '*'*120
    print q
    print p
    R = foobar(q, p)
    print '*'*120
    print foobar
    print 'currentSize =', R.currentSize
    print 'pageNumber =', R.pageNumber
    print 'totalSize =', R.totalSize
    print 'totalPage =', R.totalPage
    for r in R.data: print r.id, r.show, r.click
    print '*'*120
    transport.close()
 
except Thrift.TException, ex:
  print "%s" % (ex.message)
