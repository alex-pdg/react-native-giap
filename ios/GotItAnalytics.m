#import "GotItAnalytics.h"
#import "GIAP.h"

@implementation GotItAnalytics

// to avoid having locks on every lookup, instances is kept in an immutable dictionary and reassigned when new instances are added
GIAP *instance = nil;

#pragma mark - JS Bridge

// Expert a module named
RCT_EXPORT_MODULE(GotItAnalytics);

RCT_EXPORT_METHOD(setup: (NSString *)token
                  url:(NSString *)url
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
  
  @synchronized (self) {
    if (instance != nil) {
      resolve(nil);
      return;
    }
    
    instance = [GIAP initWithToken:token serverUrl:[NSURL URLWithString:url]];
    
    resolve(nil);
    
  }
}

RCT_EXPORT_METHOD(createAlias: (NSString *)userId
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
  [instance alias:userId];
  resolve(nil);
}

RCT_EXPORT_METHOD(identify: (NSString *)userId
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
  [instance identify:userId];
  resolve(nil);
}

RCT_EXPORT_METHOD(track: (NSString *)event
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
  [instance track:event properties:nil];
  resolve(nil);
}

RCT_EXPORT_METHOD(trackWithProperties: (NSString *)event
                  properties:(NSDictionary *)props
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
  [instance track:event properties:props];
  resolve(nil);
}

RCT_EXPORT_METHOD(set: (NSDictionary *)props
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
  [instance setProfileProperties:props];
  resolve(nil);
}

RCT_EXPORT_METHOD(reset:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
  [instance reset];
  resolve(nil);
}

@end
