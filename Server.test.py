#!/usr/bin/env python
# -*- coding : utsf8 -*-
DEFAULT_HOST, DEFAULT_PORT = None, 18920
import sys, json, random, traceback, pymongo, math, time
sys.path.append('./gen-py')
from DataReport import ReportService
from DataReport import ttypes
from DataReport.ttypes import *
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

def Main(host, port): TServer.TThreadPoolServer(ReportService.Processor(Worker()), TSocket.TServerSocket(None if host == None or host == '' else str(host), int(port)), TTransport.TBufferedTransportFactory(), TBinaryProtocol.TBinaryProtocolFactory()).serve()

class Worker:
    def __init__(self):
        self.m, self.db = pymongo.MongoClient("10.161.133.58", 12331), 'StatV3'
    def _del__(self):
        pass
    def _parse(self, collection, q, p, input, output):
        try:
            print '*'*20, time.time(), 'IN'
            db = {}
            for record in self.m[self.db][collection].find({input:int(q.id), "DAY":{"$lte":int(q.endAt), "$gte":int(q.startAt)}}, {output : True, 'bid' : True, 'bidres' : True, 'push' : True, 'show' : True, 'click' : True, 'cost' : True, 'selfcost' : True, "_id" : False}):
                key = record[output] #if isinstance(output, str) else ','.join([str(record[i]) if i in record else '' for i in output])
                #try:
                if key in db:
                    a = db[key]
                    db[key] = (a[0] + record['bid'], a[1] + record['bidres'], a[2] + record['push'], a[3] + record['show'], a[4] + record['click'], a[5] + record['cost'], a[6] + record['selfcost'], )
                #except:
                else:
                    db[key] = (record['bid'], record['bidres'], record['push'], record['show'], record['click'], record['cost'], record['selfcost'], )
            r = reportResult()
            r.totalSize = len(db)
            r.data = [Response(str(k) if isinstance(k, int) else k.encode("utf-8"), str(db[k][2]), str(db[k][3]), str(db[k][4]), str(db[k][5]), str(db[k][0]), str(db[k][1]), str(db[k][6])) for k in sorted(db.keys())[p.pageSize*(p.pageNumber-1):p.pageSize*p.pageNumber]]
            r.pageNumber = p.pageNumber
            r.currentSize = len(r.data)
            r.totalPage = math.ceil(1.0 * r.totalSize / p.pageSize)
            print '+'*20, time.time(), 'OUT/OK\n\n'
            return r
        except Exception, e:
            traceback.print_tb(sys.exc_info()[2], limit=1, file=sys.stdout)
            self.m, self.db = pymongo.MongoClient("10.161.133.58", 12331), 'StatV3'
            r = reportResult()
            r.totalSize, r.currentSize, r.totalPage, r.pageNumber = 0, 0, 0, 0
            r.data = []
            print '+'*100, time.time(), 'OUT/Error\n\n'
            return r
    def AdReportByAdid(self, q, p): return self._parse('SourceDetail', q, p, "ADID", "ADID")
    def AdReportByGroupId(self, q, p): return self._parse('SourceDetail', q, p, "GROUPID", "ADID")
    def GroupReportByPlanId(self, q, p): return self._parse('SourceDetail', q, p, "PLANID", "GROUPID")
    def PlanReportByUid(self, q, p): return self._parse('SourceDetail', q, p, "USERID", "PLANID")
    
    def AreaByAdid(self, q, p): return self._parse('AreaDetail', q, p, "ADID", "AREA")
    def AreaByGid(self, q, p): return self._parse('AreaDetail', q, p, "GROUPID", "AREA")
    def AreaByPid(self, q, p): return self._parse('AreaDetail', q, p, "PLANID", "AREA")
    def AreaByUid(self, q, p): return self._parse('AreaDetail', q, p, "USERID", "AREA")
    
    def DayByAdid(self, q, p): return self._parse('SourceDetail', q, p, "ADID", "DAY")
    def DayByGid(self, q, p): return self._parse('SourceDetail', q, p, "GROUPID", "DAY")
    def DayByPid(self, q, p): return self._parse('SourceDetail', q, p, "PLANID", "DAY")
    def DayByUid(self, q, p): return self._parse('SourceDetail', q, p, "USERID", "DAY")
    
    def SourceByAdid(self, q, p): return self._parse('SourceDetail', q, p, "ADID", "SOURCE")
    def SourceByGid(self, q, p): return self._parse('SourceDetail', q, p, "GROUPID", "SOURCE")
    def SourceByPid(self, q, p): return self._parse('SourceDetail', q, p, "PLANID", "SOURCE")
    def SourceByUid(self, q, p): return self._parse('SourceDetail', q, p, "USERID", "SOURCE")
    
    def HourByAdid(self, q, p): return self._parse('HourDetail', q, p, "ADID", "HOUR")
    def HourByGid(self, q, p): return self._parse('HourDetail', q, p, "GROUPID", "HOUR")
    def HourByPid(self, q, p): return self._parse('HourDetail', q, p, "PLANID", "HOUR")
    def HourByUid(self, q, p): return self._parse('HourDetail', q, p, "USERID", "HOUR")
    
    def HostByAdid(self, q, p): return self._parse('HostDetail', q, p, "ADID", "HOST")
    def HostByGid(self, q, p): return self._parse('HostDetail', q, p, "GROUPID", "HOST")
    def HostByPid(self, q, p): return self._parse('HostDetail', q, p, "PLANID", "HOST")
    def HostByUid(self, q, p): return self._parse('HostDetail', q, p, "USERID", "HOST")
    
    def AdspaceByAdid(self, q, p): return self._parse('AdspaceDetail', q, p, "ADID", "ADSPACE")
    def AdspaceByGid(self, q, p): return self._parse('AdspaceDetail', q, p, "GROUPID", "ADSPACE")
    def AdspaceByPid(self, q, p): return self._parse('AdspaceDetail', q, p, "PLANID", "ADSPACE")
    def AdspaceByUid(self, q, p): return self._parse('AdspaceDetail', q, p, "USERID", "ADSPACE")
    
    def getCostByUid(self, q):
        p = pageOptions()
        p.pageNumber = 1
        p.pageSize = 1000
        R = self._parse('SourceDetail', q, p, 'USERID', 'PLANID')
        if R != None and R.data != None and len(R.data) > 0:
            return sum([float(r.cost) for r in R.data])
        else:
            return 0

if __name__ == '__main__': Main(DEFAULT_HOST if len(sys.argv) <= 1 else sys.argv[1], DEFAULT_PORT if len(sys.argv) <= 2 else sys.argv[2])
