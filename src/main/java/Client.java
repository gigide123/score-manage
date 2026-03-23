import java.util.List;
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;


public class Client {
    private static final String API_BASE = "http://localhost:8070";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("师生信息管理系统\n");

        while (true) {
            System.out.print("[回车进入主菜单]");
            scanner.nextLine();
            System.out.println("======主菜单=====");
            System.out.println("1. 录入学生成绩");
            System.out.println("2. 查询某学生（每科成绩、级部排名）");
            System.out.println("3. 所有学生级部排名表（单科/总分）");
            System.out.println("4. 录入老师信息");
            System.out.println("5. 分析某教师授课班级成绩信息（及格率、平均分、最高分、最低分）");
            System.out.println("6. 展示所有教师信息");
            System.out.println("0. 退出");
            System.out.print("请输入操作编号：");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        inputStudent(scanner);
                        break;
                    case "2":
                        getStudent(scanner);
                        break;
                    case "3":
                        getStudentsRank();
                        break;
                    case "4":
                        inputTeacher(scanner);
                        break;
                    case "5":
                        printOneTeacher(scanner);
                        break;
                    case "6":
                        allTeachers();
                        break;
                    case "0":
                        System.out.println("系统已退出，感谢您的使用");
                        scanner.close();
                        return;
                    default:
                        System.out.println("输入错误");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    // 1. 录入学生成绩
    private static void inputStudent(Scanner scanner) throws Exception {
        System.out.print("输入学生编号：");
        String sid = scanner.nextLine();
        System.out.print("输入学生姓名：");
        String name = scanner.nextLine();
        System.out.print("语文成绩：");
        int chinese = Integer.parseInt(scanner.nextLine());
        System.out.print("数学成绩：");
        int math = Integer.parseInt(scanner.nextLine());
        System.out.print("英语成绩：");
        int english = Integer.parseInt(scanner.nextLine());
        System.out.print("输入学生所属班级（数字）：");
        int class_ = Integer.parseInt(scanner.nextLine());

        // 组装请求体
        Map<String, Object> requestBody = Map.of(
                "sid", sid,
                "name", name,
                "chinese", chinese,
                "math", math,
                "english", english,
                "class_", class_
        );

        // 发送POST请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/api/input_student"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> result = objectMapper.readValue(response.body(), Map.class);
        System.out.println(result.get("msg"));

    }

    // 2. 查询单个学生
    private static void getStudent(Scanner scanner) throws Exception {
        System.out.print("输入学生编号：");
        String sid = scanner.nextLine();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/api/get_student/" + sid))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> result = objectMapper.readValue(response.body(), Map.class);

        if ((int) result.get("code") == 300) {
            System.out.println(result.get("msg"));
        } else {
            List<Map<String, Object>> data = (List<Map<String, Object>>) result.get("data");
            for (Map<String, Object> item : data) {
                System.out.printf("%s--%s--%s分--第%s名\n",item.get("name"), item.get("subject"), item.get("score"), item.get("rank"));
            }
        }
    }

    // 3. 所有学生排名
    private static void getStudentsRank() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/api/get_students_rank"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, List<Map<String, Object>>> result = objectMapper.readValue(response.body(), Map.class);

        for (Map.Entry<String, List<Map<String, Object>>> entry : result.entrySet()) {
            System.out.println("======" + entry.getKey() + "成绩排名======");
            for (Map<String, Object> oneStudent : entry.getValue()) {
                System.out.printf("第%s名-%s-%s-%s分\n", oneStudent.get("rank"), oneStudent.get("sid"), oneStudent.get("name"), oneStudent.get("score"));
            }
        }
    }

    // 4. 录入教师
    private static void inputTeacher(Scanner scanner) throws Exception {
        System.out.print("输入老师编号：");
        String tid = scanner.nextLine();
        System.out.print("输入老师姓名：");
        String name = scanner.nextLine();
        System.out.print("输入所教科目（chinese/math/english）：");
        String subject = scanner.nextLine();
        System.out.print("输入老师所教班级（数字）：");
        int class_ = Integer.parseInt(scanner.nextLine());

        Map<String, Object> requestBody = Map.of(
                "tid", tid,
                "name", name,
                "subject", subject,
                "class_", class_
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/api/input_one_teacher"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> result = objectMapper.readValue(response.body(), Map.class);
        System.out.println(result.get("msg"));
    }

    // 5. 查询单个教师成绩分析
    private static void printOneTeacher(Scanner scanner) throws Exception {
        System.out.print("请输入教师编号：");
        String tid = scanner.nextLine();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/api/print_one_teacher/" + tid))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> result = objectMapper.readValue(response.body(), Map.class);

        if ((int) result.get("code") == 404) {
            System.out.println(result.get("msg"));
        } else {
            Map<String, Object> data = (Map<String, Object>) result.get("data");
            Map<String, Object> tInfo = (Map<String, Object>) data.get("teacher_info");
            System.out.printf("教师编号：%s，姓名：%s，所教科目：%s，所教班级：%s\n",
                    tInfo.get("tid"), tInfo.get("name"), tInfo.get("subject"), tInfo.get("class_"));

            Map<String, Object> tScore = (Map<String, Object>) data.get("teacher_score");
            System.out.printf("及格率：%s\n", tScore.get("pass_rate"));
            System.out.printf("平均分：%s\n", tScore.get("avg_score"));
            System.out.printf("最高分：%s 分\n", tScore.get("max_score"));
            System.out.printf("最低分：%s 分\n", tScore.get("min_score"));
        }
    }

    // 6. 所有教师信息
    private static void allTeachers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/api/all_teachers"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Map<String, Object>> data = objectMapper.readValue(response.body(), List.class);

        System.out.println("\n===== 所有教师信息 =====");
        for (Map<String, Object> t : data) {
            System.out.printf("教师编号：%s，姓名：%s，所教科目：%s，所教班级：%s\n",
                    t.get("tid"), t.get("name"), t.get("subject"), t.get("class_"));
        }
    }
}