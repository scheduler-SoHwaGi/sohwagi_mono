// import { http, HttpResponse } from 'msw';

// // const mockCounts = {
// //   scheduleCounts: [
// //     {
// //       date: "2025-06-15",
// //       counts: 2,
// //       status: "IN_PROGRESS",
// //     },
// //     {
// //       date: "2025-06-16",
// //       counts: 0,
// //       status: "NONE",
// //     },
// //     {
// //       date: "2025-06-17",
// //       counts: 5,
// //       status: "IN_PROGRESS",
// //     },
// //     {
// //       date: "2025-06-18",
// //       counts: 0,
// //       status: "DONE",
// //     },
// //     {
// //       date: "2025-06-19",
// //       counts: 3,
// //       status: "IN_PROGRESS",
// //     },
// //     {
// //       date: "2025-06-20",
// //       counts: 8,
// //       status: "IN_PROGRESS",
// //     },
// //     {
// //       date: "2025-06-21",
// //       counts: 2,
// //       status: "IN_PROGRESS",
// //     },
// //   ]
// // };


// // export const mockSchedules = {
// //   schedules: [
// //     {
// //       scheduleId: 4,
// //       title: '면접 준비',
// //       time: '오전 7:30',
// //       checked: true,
// //     },
// //     {
// //       scheduleId: 5,
// //       title: 'CMC 면접',
// //       time: '오전 8:30',
// //       checked: false,
// //     },
// //   ],
// // };


// const baseURL = import.meta.env.VITE_BASE_SERVER_URL;

// export const handlers = [
//   http.get(`${baseURL}/schedules/counts`, () => {
//     return HttpResponse.json(mockCounts)
//   }),

//   // http.get(`${baseURL}/schedules`, () => {
//   //   return HttpResponse.json(mockSchedules)
//   // }),

//   http.post(`${baseURL}/schedules/:scheduleId/actions/toggle-checked`, () => {
//     return HttpResponse.json({}, { status: 200 });
//   })

];