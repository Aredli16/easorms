'use client';

import { SchoolYear } from '@/types/school-year';
import { classNames } from '@/utils/css';
import { useEffect, useState } from 'react';
import { setCurrentSchoolYear } from '@/lib/client/school-year';

const SchoolYearList = ({ schoolYears }: { schoolYears: SchoolYear[] }) => {
  const [current, setCurrent] = useState<SchoolYear | undefined>(undefined);

  const updateCurrent = async (id: string) => {
    setCurrent(undefined);

    await setCurrentSchoolYear(id);

    const currentSchoolYear = schoolYears.find(
      (schoolYear) => schoolYear.current
    );

    if (currentSchoolYear) currentSchoolYear.current = false;

    schoolYears.find((schoolYear) => schoolYear.id === id)!.current = true;

    setCurrent(schoolYears.find((schoolYear) => schoolYear.current));
  };

  useEffect(() => {
    setCurrent(schoolYears.find((schoolYear) => schoolYear.current));
  }, [schoolYears]);

  return (
    <>
      <ul className="border border-gray-700 divide-y divide-gray-700 rounded-md">
        {schoolYears.map((schoolYear) => (
          <li
            key={schoolYear.id}
            className="px-6 py-4 flex items-center justify-between text-sm leading-5"
          >
            <div className="flex items-center">
              <button
                type="button"
                onClick={async () => updateCurrent(schoolYear.id!)}
                className={classNames(
                  current?.id === schoolYear.id
                    ? 'bg-green-500'
                    : 'bg-gray-500',
                  'flex-shrink-0 inline-block h-2 w-2 rounded-full'
                )}
              />
              <span className="ml-3 font-medium text-white">
                {schoolYear.startDate.toLocaleString()} -{' '}
                {schoolYear.endDate.toLocaleString()}
              </span>
            </div>
            {/*<div className="flex items-center">
              <button
                type="button"
                className="inline-flex items-center px-3 py-1 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-gray-700 hover:bg-gray-600 focus:outline-none focus:border-gray-900 focus:shadow-outline-gray active:bg-gray-900 transition ease-in-out duration-150"
              >
                Edit
              </button>
            </div>*/}
          </li>
        ))}
      </ul>
    </>
  );
};

export default SchoolYearList;
