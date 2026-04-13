// src/lib/amplitude.ts
import * as amplitude from '@amplitude/analytics-browser';

const apiKey = import.meta.env.VITE_AMPLITUDE_API_KEY;
const isProduction = import.meta.env.MODE === 'production';

export const initAmplitude = () => {
  if (!apiKey || !isProduction) return;
  amplitude.init(apiKey, undefined, {
    autocapture: {
      attribution: false,
      fileDownloads: false,
      networkTracking: true,
      pageViews: false,
    },
  });
};

export const trackEvent = (eventName: string, eventProperties?: Record<string, unknown>) => {
  if (!isProduction) return;
  amplitude.track(eventName, eventProperties);
};


