import { isEmpty } from 'lodash';
import { NativeModules } from 'react-native';

const { GotItAnalytics } = NativeModules;

export const initialize = (token: string, url: string) => {
  return GotItAnalytics.setup(token, url);
};

export const identify = (uniqueId: string) => {
  GotItAnalytics.identify(uniqueId);
  GotItAnalytics.set({ uid: uniqueId });
};

export const alias = (uniqueId: string) => {
  GotItAnalytics.createAlias(uniqueId);
  GotItAnalytics.set({ uid: uniqueId });
};

export const track = (event = '', properties = {}) => {
  if (isEmpty(properties)) {
    GotItAnalytics.track(event);
  } else {
    GotItAnalytics.trackWithProperties(event, properties);
  }
};

export const reset = () => {
  GotItAnalytics.reset();
};

export const addPushToken = (token: string) => {
  GotItAnalytics.set({ push_token: token });
};
