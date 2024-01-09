import { SchoolYear } from '@/types/school_year';
import { getSchoolYear } from '@/lib/server/school-year';
import SchoolYearList from '@/components/admin/SchoolYearList';

const Page = async () => {
  const schoolYears: SchoolYear[] = await getSchoolYear();

  return (
    <div className="divide-y divide-white/5">
      <div className="grid max-w-7xl grid-cols-1 gap-x-8 gap-y-10 px-4 py-16 sm:px-6 md:grid-cols-3 lg:px-8">
        <div>
          <h2 className="text-base font-semibold leading-7 text-white">School year</h2>
          <p className="mt-1 text-sm leading-6 text-gray-400">
            Configure the current school year. This will be used to determine the current registration period.
          </p>
        </div>
        <form>
          <h2 className="text-base font-semibold text-white">Create school year</h2>
          <div className="grid grid-cols-2 gap-y-6 sm:grid-cols-2 sm:gap-x-8">
            <div>
              <label htmlFor="startDate" className="block text-sm font-medium leading-5 text-gray-400">
                Start date
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <input
                  id="startDate"
                  name="startDate"
                  className="form-input block w-full sm:text-sm sm:leading-5 bg-gray-700"
                  type="date"
                  required
                />
              </div>
            </div>

            <div>
              <label htmlFor="endDate" className="block text-sm font-medium leading-5 text-gray-400">
                End date
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <input
                  id="endDate"
                  name="endDate"
                  className="form-input block w-full sm:text-sm sm:leading-5 bg-gray-700"
                  type="date"
                  required
                />
              </div>
            </div>

            <div className="relative flex items-start col-span-full">
              <div className="flex h-6 items-center">
                <input
                  id="currentSchoolYear"
                  name="currentSchoolYear"
                  type="checkbox"
                  className="h-4 w-4 rounded border-gray-300 text-indigo-600 focus:ring-indigo-600"
                />
              </div>
              <div className="ml-3 text-sm leading-6">
                <label htmlFor="currentSchoolYear" className="font-medium">
                  Current School Year
                </label>
              </div>
            </div>
          </div>

          <div className="mt-6">
            <span className="block w-full rounded-md shadow-sm">
              <button
                type="submit"
                className="flex justify-center w-full px-4 py-2 text-sm font-medium leading-5 text-white transition duration-150 ease-in-out bg-gray-800 border border-transparent rounded-md hover:bg-gray-700 focus:outline-none focus:border-gray-900 focus:shadow-outline-gray active:bg-gray-900"
              >
                Save
              </button>
            </span>
          </div>
        </form>
        <div className="col-start-2 col-span-full">
          <h2 className="text-base font-semibold text-white">School year</h2>
          <div className="mt-6">
            <SchoolYearList schoolYears={schoolYears} />
          </div>
        </div>
      </div>

      <div className="grid max-w-7xl grid-cols-1 gap-x-8 gap-y-10 px-4 py-16 sm:px-6 md:grid-cols-3 lg:px-8">
        <div>
          <h2 className="text-base font-semibold leading-7 text-white">Customs fields</h2>
          <p className="mt-1 text-sm leading-6 text-gray-400">
            Configure the custom fields that will be displayed on the registration form.
          </p>
        </div>

        <div>ss</div>
      </div>
    </div>
  );
};

export default Page;
