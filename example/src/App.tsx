import * as React from 'react';

import { StyleSheet, View } from 'react-native';
import { track } from 'react-native-giap';

export default function App() {
  React.useEffect(() => {
    console.log('track event');
    track('Active');
  }, []);

  return <View style={styles.container} />;
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
